package com.decompiler;

import com.sdc.languages.general.visitors.GeneralClassVisitor;
import com.sdc.languages.java.visitors.JavaClassVisitor;
import com.sdc.languages.js.visitors.JSClassVisitor;
import com.sdc.languages.kotlin.visitors.KotlinClassVisitor;

import com.beust.jcommander.JCommander;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


public class Decompiler {
    public static void main(String[] args) throws IOException {
        final DecompilerParameters decompilerParameters = new DecompilerParameters();
        final JCommander jCommander = new JCommander(decompilerParameters, args);

        if (decompilerParameters.isHelp()) {
            jCommander.usage();
            return;
        }

        if (decompilerParameters.getClassName() == null && decompilerParameters.getClassPath() == null) {
            System.out.println("One of the following option is required: -c, -p. Use --help for more usage information.");
            return;
        }

        final String language = decompilerParameters.getLanguage();
        final int tabSize = decompilerParameters.getTabSize();
        final int textWidth = decompilerParameters.getTextWidth();

        ClassReader cr;

        if (decompilerParameters.getClassName() != null) {
            cr = new ClassReader(decompilerParameters.getClassName());
        } else {
            cr = new ClassReader(new FileInputStream(decompilerParameters.getClassPath()));
        }

        System.out.println(getDecompiledCode(language, cr, "", textWidth, tabSize));
    }

    @NotNull
    public static String convertExceptionToString(final @NotNull Exception exception) {
        final StringBuilder sb = new StringBuilder("\n//\t");

        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);

        sb.append(sw.toString().replace("\n\t", "\n//\t"));

        return sb.toString();
    }

    @NotNull
    public static String getDecompiledCode(final @NotNull String languageName,
                                           final @NotNull ClassReader cr,
                                           final @NotNull String classFilesJarPath,
                                           final int textWidth,
                                           final int tabSize) throws IOException {
        ClassVisitor specifiedLanguageClassVisitor;
        final StringWriter sw = new StringWriter();

        final String lowerCaseName = languageName.toLowerCase();
        if (lowerCaseName.equals("javascript")) {
            specifiedLanguageClassVisitor = new JSClassVisitor(textWidth, tabSize);
        } else if (lowerCaseName.equals("kotlin")) {
            specifiedLanguageClassVisitor = new KotlinClassVisitor(textWidth, tabSize);
        } else if (lowerCaseName.equals("java")) {
            specifiedLanguageClassVisitor = new JavaClassVisitor(textWidth, tabSize);
        } else {
            specifiedLanguageClassVisitor = new TraceClassVisitor(new PrintWriter(sw));
        }

        if (specifiedLanguageClassVisitor instanceof GeneralClassVisitor) {
            ((GeneralClassVisitor) specifiedLanguageClassVisitor).setClassFilesJarPath(classFilesJarPath);
        }

        try {
            cr.accept(specifiedLanguageClassVisitor, 0);
            if (specifiedLanguageClassVisitor instanceof GeneralClassVisitor) {
                return ((GeneralClassVisitor) specifiedLanguageClassVisitor).getDecompiledCode();
            } else {
                return sw.toString();
            }
        } catch (RuntimeException e) {
            return "General class decompiling error occurred:" + convertExceptionToString(e);
        }
    }
}
