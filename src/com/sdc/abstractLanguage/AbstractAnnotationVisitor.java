package com.sdc.abstractLanguage;

import org.objectweb.asm.AnnotationVisitor;

import static org.objectweb.asm.Opcodes.ASM4;

public class AbstractAnnotationVisitor extends AnnotationVisitor {
    protected AbstractAnnotation myAnnotation;

    public AbstractAnnotationVisitor(final AbstractAnnotation annotation) {
        super(ASM4);
        this.myAnnotation = annotation;
    }

    @Override
    public void visit(final String name, final Object value) {
        myAnnotation.addProperty(name, value);
        super.visit(name, value);
    }

    @Override
    public void visitEnum(final String name, final String desc, final String value) {
        super.visitEnum(name, desc, value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String name, final String desc) {
        return super.visitAnnotation(name, desc);
    }

    @Override
    public AnnotationVisitor visitArray(final String name) {
        return super.visitArray(name);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
