package com.actions;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;

public interface DecompilationChoiceListener {
    void treatFile(final FileEditorManager manager, final VirtualFile file);
}
