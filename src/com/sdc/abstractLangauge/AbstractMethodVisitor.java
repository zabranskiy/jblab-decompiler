package com.sdc.abstractLangauge;

import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.ASM4;

public abstract class AbstractMethodVisitor  extends MethodVisitor {
    public AbstractMethodVisitor() {
        super(ASM4);
    }
}
