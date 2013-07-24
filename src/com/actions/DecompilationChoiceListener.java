package com.actions;

import com.intellij.openapi.vfs.VirtualFile;

public interface DecompilationChoiceListener {
    void decompile(VirtualFile virtualFile);
}
