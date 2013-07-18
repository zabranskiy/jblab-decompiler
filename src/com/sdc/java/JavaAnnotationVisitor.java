package com.sdc.java;

import org.objectweb.asm.AnnotationVisitor;
import static org.objectweb.asm.Opcodes.ASM4;

public class JavaAnnotationVisitor extends AnnotationVisitor {
    private JavaAnnotation myAnnotation;

    public JavaAnnotationVisitor(JavaAnnotation javaAnnotation) {
        super(ASM4);
        this.myAnnotation = javaAnnotation;
    }

    @Override
    public void visit(String name, Object value) {
        myAnnotation.addProperty(name, value);
        super.visit(name, value);
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        super.visitEnum(name, desc, value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        return super.visitAnnotation(name, desc);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        return super.visitArray(name);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
