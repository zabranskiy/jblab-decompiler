package com.decompiler;

import com.beust.jcommander.JCommander;

import com.sdc.abstractLanguage.AbstractClassVisitor;
import com.sdc.java.JavaClassVisitor;
import com.sdc.js.JSClassVisitor;

import org.objectweb.asm.ClassReader;
//import org.objectweb.asm.ClassVisitor;
//import org.objectweb.asm.util.TraceClassVisitor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.io.PrintWriter;


public class Decompiler {
    public static void main(String[] args) throws IOException {
        DecompilerParameters decompilerParameters = new DecompilerParameters();
        final JCommander jCommander = new JCommander(decompilerParameters, args);

        if (decompilerParameters.isHelp()) {
            jCommander.usage();
            return;
        }

        if (decompilerParameters.getClassName() == null && decompilerParameters.getClassPath() == null) {
            System.out.println("One of the following option is required: -c, -p. Use --help for more usage information.");
            return;
        }

        final int tabSize = decompilerParameters.getTabSize();
        final int textWidth = decompilerParameters.getTextWidth();

        ClassReader cr;

        if (decompilerParameters.getClassName() != null) {
            cr = new ClassReader(decompilerParameters.getClassName());
        } else {
            cr = new ClassReader(new FileInputStream(decompilerParameters.getClassPath()));
        }

        AbstractClassVisitor cv;
//        ClassVisitor cv = new TraceClassVisitor(new PrintWriter(System.out));

        if (decompilerParameters.getLanguage().equals("java")) {
            cv = new JavaClassVisitor(textWidth, tabSize);
        } else if (decompilerParameters.getLanguage().equals("js")) {
            cv = new JSClassVisitor(textWidth, tabSize);
        }/* else if (decompilerParameters.getLanguage().equals("cpp")) {
            cv = new CppClassVisitor(textWidth, tabSize);
        }*/ else {
            System.out.println("Specify one of the valid output language. Use --help for more usage information.");
            return;
        }

        cr.accept(cv, 0);
        System.out.println(cv.getDecompiledCode());
    }


    public static String decompile(Language lang, final InputStream is, Integer textWidth, Integer tabSize) throws IOException {
        ClassReader cr = new ClassReader(is);

        AbstractClassVisitor cv;
        if (lang.getName().equals("JavaScript")) {
            cv = new JSClassVisitor(textWidth, tabSize);
        } else {
            // Java
            cv = new JavaClassVisitor(textWidth, tabSize);
        }
        cr.accept(cv, 0);
        return cv.getDecompiledCode();
    }
}
