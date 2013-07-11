package com.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.testFramework.LightVirtualFile;
import com.sdc.java.JavaClassVisitor;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Action for triggering decompilation.
 * Dmitriy Zabranskiy, 2013
 */

public class Decompile extends AnAction {
    private final static int LEN = 4;

    public void actionPerformed(AnActionEvent e) {
        VirtualFile virtualFile = DataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        if (virtualFile != null && !virtualFile.isDirectory()) {
            try {
                // Detection class file with any file extension
                InputStream is = virtualFile.getInputStream();
                byte[] bytes = new byte[LEN];
                is.mark(LEN);
                is.read(bytes);
                final int magic = ByteBuffer.wrap(bytes).getInt();
                if (magic == 0xCAFEBABE) {
                    is.reset();
                    LightVirtualFile decompiledFile = new LightVirtualFile(virtualFile.getNameWithoutExtension() + ".java", decompile(is));
                    final Project currentProject = e.getData(PlatformDataKeys.PROJECT);
                    assert currentProject != null;
                    final PsiFile psiFile = PsiManager.getInstance(currentProject).findFile(decompiledFile);
                    // Reformat decompiled code by IDEA
                    ApplicationManager.getApplication().runWriteAction(new Runnable() {
                        @Override
                        public void run() {
                            assert psiFile != null;
                            CodeStyleManager.getInstance(currentProject).reformat(psiFile);
                        }
                    });
                    FileEditorManager.getInstance(currentProject).openFile(decompiledFile, true);
                }
                is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public String decompile(final InputStream is) throws IOException {
        ClassReader cr = new ClassReader(is);
        // params
        final int tabSize = 4;
        final int textWidth = 100;

        JavaClassVisitor cv = new JavaClassVisitor(textWidth, tabSize);
        cr.accept(cv, 0);
        return cv.getDecompiledCode();
    }
}

