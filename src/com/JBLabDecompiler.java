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
import org.jetbrains.annotations.NotNull;

import static com.decompiler.Decompiler.decompile;


public class JBLabDecompiler implements ApplicationComponent, DecompilationChoiceListener, ProjectManagerListener {
    private Project myCurrentProject;

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
