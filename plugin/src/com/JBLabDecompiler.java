package com;

import com.actions.DecompilationChoiceListener;
import com.actions.NavigationListener;
import com.config.PluginConfigComponent;
import com.decompiler.Language;
import com.decompiler.Settings;
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
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


public class JBLabDecompiler implements ApplicationComponent, DecompilationChoiceListener, ProjectManagerListener {
    private Project myCurrentProject;

    @Nullable
    public static VirtualFile decompile(final PluginConfigComponent config, final VirtualFile virtualFile) {
        //Number of bytes CAFEBABE
        final int CAFEBABE = 4;

        LightVirtualFile decompiledFile = null;

        try {
            InputStream is = virtualFile.getInputStream();
            byte[] bytes = new byte[CAFEBABE];
            is.mark(CAFEBABE);
            is.read(bytes);
            final int magic = ByteBuffer.wrap(bytes).getInt();
            if (magic == 0xCAFEBABE) {
                is.reset();
                final Language lang = config.getChosenLanguage();

                final String initialClassFilePath = virtualFile.getPath();
                final int jarIndex = initialClassFilePath.indexOf(".jar!/");
                final String jarPath = jarIndex == -1 ? "" : initialClassFilePath.substring(0, jarIndex + 4);

                decompiledFile = new LightVirtualFile(virtualFile.getNameWithoutExtension() + lang.getExtension(),
                        getDecompiledCode(lang.getName(), is, jarPath, config.getTextWidth(), config.getTabSize()));
            }
            is.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return decompiledFile;
    }

    private static String getDecompiledCode(final String languageName, final InputStream is, final String classFilesJarPath, final Integer textWidth, final Integer tabSize) throws IOException {
        return com.decompiler.Decompiler.getDecompiledCode(languageName, new ClassReader(is), classFilesJarPath, textWidth, tabSize);
    }

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
    public void projectOpened(final Project project) {
        this.myCurrentProject = project;
        Settings.getInstance().setPath(myCurrentProject.getBasePath());
        NavigationListener navigationListener = new NavigationListener(this);
        project.getMessageBus().connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, navigationListener);
    }

    @Override
    public boolean canCloseProject(final Project project) {
        return true;
    }

    @Override
    public void projectClosed(final Project project) {
        // no-op
    }

    @Override
    public void projectClosing(final Project project) {
        // no-op
    }

    @Override
    public void treatFile(final FileEditorManager manager, final VirtualFile file) {
        PluginConfigComponent config = ApplicationManager.getApplication().getComponent(PluginConfigComponent.class);

        VirtualFile decompiledFile = decompile(config, file);

        if (decompiledFile != null) {
            if (!config.isShowPrettyEnabled()) {
                final PsiFile psiFile = PsiManager.getInstance(myCurrentProject).findFile(decompiledFile);
                // Reformat decompiled code by IDEA
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    @Override
                    public void run() {
                        assert psiFile != null;
                        CodeStyleManager.getInstance(myCurrentProject).reformat(psiFile);
                    }
                });
            }
            manager.closeFile(file);
            manager.openFile(decompiledFile, true);
        }
    }
}
