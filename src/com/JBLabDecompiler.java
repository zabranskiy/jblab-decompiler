package com;

import com.actions.DecompilationChoiceListener;
import com.actions.NavigationListener;
import com.config.PluginConfigComponent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.testFramework.LightVirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static com.decompiler.Decompiler.getDecompiledCode;


public class JBLabDecompiler implements ApplicationComponent, DecompilationChoiceListener, ProjectManagerListener {
    private Project project;

    public void initComponent() {
        ProjectManager.getInstance().addProjectManagerListener(this);
    }

    public void disposeComponent() {
        ProjectManager.getInstance().removeProjectManagerListener(this);
    }

    @NotNull
    public String getComponentName() {
        return "JBLab Decompiler";
    }

    @Override
    public void projectOpened(Project project) {
        this.project = project;
        NavigationListener navigationListener = new NavigationListener(this);
        project.getMessageBus().connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, navigationListener);
    }

    @Override
    public boolean canCloseProject(Project project) {
        return true;
    }

    @Override
    public void projectClosed(Project project) {
        // no-op
    }

    @Override
    public void projectClosing(Project project) {
        // no-op
    }

    @Override
    public void decompile(VirtualFile virtualFile) {
        FileEditorManager manager = FileEditorManager.getInstance(project);
        PluginConfigComponent pluginComponent = ApplicationManager.getApplication().getComponent(PluginConfigComponent.class);
        try {
            InputStream is = virtualFile.getInputStream();
            byte[] bytes = new byte[com.Constants.CAFEBABE];
            is.mark(com.Constants.CAFEBABE);
            is.read(bytes);
            final int magic = ByteBuffer.wrap(bytes).getInt();
            if (magic == 0xCAFEBABE) {
                is.reset();
                LightVirtualFile decompiledFile = new LightVirtualFile(virtualFile.getNameWithoutExtension() + pluginComponent.getChosenLanguage().getExtension(),
                        getDecompiledCode(pluginComponent.getChosenLanguage(), is, pluginComponent.getTextWidth(), pluginComponent.getTabSize()));
                if (!pluginComponent.isShowPrettyEnabled()) {
                    final PsiFile psiFile = PsiManager.getInstance(project).findFile(decompiledFile);
                    // Reformat decompiled code by IDEA
                    ApplicationManager.getApplication().runWriteAction(new Runnable() {
                        @Override
                        public void run() {
                            assert psiFile != null;
                            CodeStyleManager.getInstance(project).reformat(psiFile);
                        }
                    });
                }
                manager.closeFile(virtualFile);
                manager.openFile(decompiledFile, true);
            }
            is.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
