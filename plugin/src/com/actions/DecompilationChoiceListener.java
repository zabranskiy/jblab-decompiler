package com.actions;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;

import org.jetbrains.annotations.NotNull;


public interface DecompilationChoiceListener {
    void treatFile(final @NotNull FileEditorManager manager, final @NotNull VirtualFile file);
}
