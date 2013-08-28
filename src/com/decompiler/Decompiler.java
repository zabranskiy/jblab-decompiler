package com.decompiler;

import com.beust.jcommander.JCommander;
import com.config.PluginConfigComponent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import com.sdc.languages.general.visitors.GeneralClassVisitor;
import com.sdc.languages.java.visitors.JavaClassVisitor;
import com.sdc.languages.js.visitors.JSClassVisitor;
import com.sdc.languages.kotlin.visitors.KotlinClassVisitor;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.*;
import java.nio.ByteBuffer;


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

    @Nullable
    public static VirtualFile decompile(final PluginConfigComponent config, final VirtualFile virtualFile) {
        //Number of bytes CAFEBABE
        final int CAFEBABE = 4;

        LightVirtualFile decompiledFile = null;

        try {
            InputStream is = virtualFile.getInputStream();
            byte[] bytes = new byte[CAFEBABE];
            is.mark(CAFEBABE);
            is.read(bytes);
            final int magic = ByteBuffer.wrap(bytes).getInt();
            if (magic == 0xCAFEBABE) {
                is.reset();
                final Language lang = config.getChosenLanguage();

                final String initialClassFilePath = virtualFile.getPath();
                final int jarIndex = initialClassFilePath.indexOf(".jar!/");
                final String jarPath = jarIndex == -1 ? "" : initialClassFilePath.substring(0, jarIndex + 4);

                decompiledFile = new LightVirtualFile(virtualFile.getNameWithoutExtension() + lang.getExtension(),
                        getDecompiledCode(lang.getName(), is, jarPath, config.getTextWidth(), config.getTabSize()));
            }
            is.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return decompiledFile;
    }

    public static String printExceptionToString(final Exception exception) {
        StringBuilder sb = new StringBuilder("\n//\t");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        sb.append(sw.toString().replace("\n\t", "\n//\t"));

        return sb.toString();
    }

    private static String getDecompiledCode(final String languageName, final InputStream is, final String classFilesJarPath, final Integer textWidth, final Integer tabSize) throws IOException {
        return getDecompiledCode(languageName, new ClassReader(is), classFilesJarPath, textWidth, tabSize);
    }

    private static String getDecompiledCode(final String languageName, final ClassReader cr, final String classFilesJarPath, final Integer textWidth, final Integer tabSize) throws IOException {
        ClassVisitor specifiedLanguageClassVisitor;
        StringWriter sw = new StringWriter();

        if (languageName.toLowerCase().equals("javascript")) {
            specifiedLanguageClassVisitor = new JSClassVisitor(textWidth, tabSize);
        } else if (languageName.toLowerCase().equals("kotlin")) {
            specifiedLanguageClassVisitor = new KotlinClassVisitor(textWidth, tabSize);
        } else if (languageName.toLowerCase().equals("java")) {
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
            return "General class decompiling error occurred:" + printExceptionToString(e);
        }
    }
}
