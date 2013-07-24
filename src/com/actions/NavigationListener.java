package com.actions;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class NavigationListener implements FileEditorManagerListener {

    private final DecompilationChoiceListener decompilationListener;

    public NavigationListener(@NotNull DecompilationChoiceListener decompilationListener) {
        this.decompilationListener = decompilationListener;
    }

    @Override
    public void fileOpened(FileEditorManager source, VirtualFile virtualFile) {
        decompilationListener.decompile(virtualFile);
    }

    @Override
    public void fileClosed(FileEditorManager source, VirtualFile file) {
        // no-op
    }

    @Override
    public void selectionChanged(FileEditorManagerEvent event) {
        // no-op
    }
}
