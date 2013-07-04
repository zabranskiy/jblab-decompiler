package com.sdc;

import com.sdc.java.JavaClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;
import java.io.InputStream;

public class DecompilerService {

    public void decompile(InputStream is) throws IOException {
        ClassReader cr = new ClassReader(is);


        final int tabSize = 4;
        final int textWidth = 100;
        ClassVisitor cv = new JavaClassVisitor(textWidth, tabSize);

        if (cr != null) {
            cr.accept(cv, 0);
        }
    }

    public void decompile(byte[] bytes) throws IOException {
        ClassReader cr = new ClassReader(bytes);

        final int tabSize = 4;
        final int textWidth = 100;
        ClassVisitor cv = new JavaClassVisitor(textWidth, tabSize);

        if (cr != null) {
            cr.accept(cv, 0);
        }
    }
}