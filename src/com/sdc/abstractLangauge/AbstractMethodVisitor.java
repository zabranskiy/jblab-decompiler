package com.sdc.abstractLangauge;

import org.objectweb.asm.*;

/**
 * Created with IntelliJ IDEA.
 * User: 1
 * Date: 21.03.13
 * Time: 17:43
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractMethodVisitor  extends MethodVisitor {
    public AbstractMethodVisitor(int api) {
        super(api);
    }

    public AbstractMethodVisitor(int api, MethodVisitor mv) {
        super(api, mv);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        return super.visitAnnotationDefault();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return super.visitAnnotation(desc, visible);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        return super.visitParameterAnnotation(parameter, desc, visible);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitAttribute(Attribute attr) {
        super.visitAttribute(attr);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitCode() {
        super.visitCode();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        super.visitFrame(type, nLocal, local, nStack, stack);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        super.visitIntInsn(opcode, operand);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        super.visitVarInsn(opcode, var);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        super.visitTypeInsn(opcode, type);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        super.visitFieldInsn(opcode, owner, name, desc);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        super.visitMethodInsn(opcode, owner, name, desc);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
        super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        super.visitJumpInsn(opcode, label);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitLabel(Label label) {
        super.visitLabel(label);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitLdcInsn(Object cst) {
        super.visitLdcInsn(cst);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        super.visitIincInsn(var, increment);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        super.visitTableSwitchInsn(min, max, dflt, labels);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        super.visitLookupSwitchInsn(dflt, keys, labels);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        super.visitMultiANewArrayInsn(desc, dims);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        super.visitTryCatchBlock(start, end, handler, type);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, desc, signature, start, end, index);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        super.visitLineNumber(line, start);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitEnd() {
        super.visitEnd();    //To change body of overridden methods use File | Settings | File Templates.
    }

}
