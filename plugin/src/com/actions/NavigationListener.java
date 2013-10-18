package com.actions;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class NavigationListener implements FileEditorManagerListener {

    private final DecompilationChoiceListener myDecompilationListener;

    public NavigationListener(@NotNull final DecompilationChoiceListener decompilationListener) {
        this.myDecompilationListener = decompilationListener;
    }

    @Override
    public void fileOpened(final FileEditorManager source, final VirtualFile file) {
        myDecompilationListener.treatFile(source, file);
    }

    @Override
    public void fileClosed(final FileEditorManager source, final VirtualFile file) {
        // no-op
    }

    @Override
    public void selectionChanged(final FileEditorManagerEvent event) {
        // no-op
    }
}
