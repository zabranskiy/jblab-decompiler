package com.sdc.abstractLangauge;

import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.ASM4;

public abstract class AbstractClassVisitor extends ClassVisitor {
    public AbstractClassVisitor() {
        super(ASM4);
    }

    public String getDecompiledCode() {
        return null;
    }
}
