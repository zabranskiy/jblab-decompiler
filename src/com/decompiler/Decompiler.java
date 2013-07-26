package com.decompiler;

import com.beust.jcommander.JCommander;
import com.config.PluginConfigComponent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import com.sdc.abstractLanguage.AbstractClassVisitor;
import com.sdc.java.JavaClassVisitor;
import com.sdc.js.JSClassVisitor;
import com.sdc.kotlin.KotlinClassVisitor;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

//import org.objectweb.asm.ClassVisitor;
//import org.objectweb.asm.util.TraceClassVisitor;
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

        final String language = decompilerParameters.getLanguage();
        final int tabSize = decompilerParameters.getTabSize();
        final int textWidth = decompilerParameters.getTextWidth();

        ClassReader cr;

        if (decompilerParameters.getClassName() != null) {
            cr = new ClassReader(decompilerParameters.getClassName());
        } else {
            cr = new ClassReader(new FileInputStream(decompilerParameters.getClassPath()));
        }

        System.out.println(getDecompiledCode(language, cr, textWidth, tabSize));
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
                decompiledFile = new LightVirtualFile(virtualFile.getNameWithoutExtension() + lang.getExtension(),
                        getDecompiledCode(lang.getName(), is, config.getTextWidth(), config.getTabSize()));
            }
            is.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return decompiledFile;
    }

    private static String getDecompiledCode(final String languageName, final InputStream is, final Integer textWidth, final Integer tabSize) throws IOException {
        return getDecompiledCode(languageName, new ClassReader(is), textWidth, tabSize);
    }

    private static String getDecompiledCode(final String languageName, final ClassReader cr, final Integer textWidth, final Integer tabSize) throws IOException {
        AbstractClassVisitor cv;

        if (languageName.equals("JavaScript")) {
            cv = new JSClassVisitor(textWidth, tabSize);
        } else if (languageName.equals("Kotlin")) {
            cv = new KotlinClassVisitor(textWidth, tabSize);
        } else {
            // Java
            cv = new JavaClassVisitor(textWidth, tabSize);
        }

        cr.accept(cv, 0);
        return cv.getDecompiledCode();
    }
}
