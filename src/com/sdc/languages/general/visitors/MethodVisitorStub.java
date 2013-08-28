package com.sdc.languages.general.visitors;

import com.decompiler.Decompiler;
import com.sdc.languages.general.visitors.AbstractMethodVisitor;
import org.objectweb.asm.*;

public class MethodVisitorStub extends AbstractMethodVisitor {
    public class DecompilerException {
        private final String myErrorLocation;
        private final Exception myException;

        public DecompilerException(final String errorLocation, final Exception exception) {
            this.myErrorLocation = errorLocation;
            this.myException = exception;
        }

        public String getErrorLocation() {
            return myErrorLocation;
        }

        public String getException() {
            return Decompiler.printExceptionToString(myException);
        }
    }

    private boolean myHasDecompilingError = false;
    private DecompilerException myError = null;

    public MethodVisitorStub(final AbstractMethodVisitor mv) {
        super(mv.getDecompiledMethod(), mv.getDecompiledOwnerFullClassName(), mv.getDecompiledOwnerSuperClassName());
        this.mv = mv;
    }

    private void processOccurredError(final String errorLocation, final Exception exception) {
        myHasDecompilingError = true;
        myError = new DecompilerException(errorLocation, exception);
    }

    @Override
    protected boolean checkForAutomaticallyGeneratedAnnotation(final String annotationName) {
        return false;
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        try {
            if (!myHasDecompilingError) {
                return mv.visitAnnotationDefault();
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting annotationDefault", e);
        }
        return null;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        try {
            if (!myHasDecompilingError) {
                return mv.visitAnnotation(desc, visible);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting annotation { desc = \"" + desc + "\" }", e);
        }
        return null;
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        try {
            if (!myHasDecompilingError) {
                return mv.visitParameterAnnotation(parameter, desc, visible);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting parameter annotation { desc = \"" + desc + "\" }", e);
        }
        return null;
    }

    @Override
    public void visitAttribute(Attribute attr) {
        try {
            if (!myHasDecompilingError) {
                mv.visitAttribute(attr);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting attribute", e);
        }
    }

    @Override
    public void visitCode() {
        try {
            if (!myHasDecompilingError) {
                mv.visitCode();
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting code", e);
        }
    }

    @Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        try {
            if (!myHasDecompilingError) {
                mv.visitFrame(type, nLocal, local, nStack, stack);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting frame", e);
        }
    }

    @Override
    public void visitInsn(int opcode) {
        try {
            if (!myHasDecompilingError) {
                mv.visitInsn(opcode);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting instruction { opcode = " + opcode + " }", e);
        }
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        try {
            if (!myHasDecompilingError) {
                mv.visitIntInsn(opcode, operand);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting int instruction { opcode = " + opcode + ", operand = " + operand + " }", e);
        }
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        try {
            if (!myHasDecompilingError) {
                mv.visitVarInsn(opcode, var);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting var instruction { opcode = " + opcode + ", var = " + var + " }", e);
        }
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        try {
            if (!myHasDecompilingError) {
                mv.visitTypeInsn(opcode, type);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting type instruction { opcode = " + opcode + ", type = \"" + type + "\" }", e);
        }
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        try {
            if (!myHasDecompilingError) {
                mv.visitFieldInsn(opcode, owner, name, desc);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting field instruction { opcode = " + opcode + ", owner = \"" + owner + ", name = \"" + name + "\" }", e);
        }
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        try {
            if (!myHasDecompilingError) {
                mv.visitMethodInsn(opcode, owner, name, desc);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting method instruction { opcode = " + opcode + ", owner = \"" + owner + ", name = \"" + name + "\" }", e);
        }
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
        try {
            if (!myHasDecompilingError) {
                mv.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting invoke dynamic instruction", e);
        }
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        try {
            if (!myHasDecompilingError) {
                mv.visitJumpInsn(opcode, label);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting jump instruction { opcode = " + opcode + " }", e);
        }
    }

    @Override
    public void visitLabel(Label label) {
        try {
            if (!myHasDecompilingError) {
                mv.visitLabel(label);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting label", e);
        }
    }

    @Override
    public void visitLdcInsn(Object cst) {
        try {
            if (!myHasDecompilingError) {
                mv.visitLdcInsn(cst);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting ldc instruction", e);
        }
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        try {
            if (!myHasDecompilingError) {
                mv.visitIincInsn(var, increment);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting iinc instruction { var = " + var + " }", e);
        }
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        try {
            if (!myHasDecompilingError) {
                mv.visitTableSwitchInsn(min, max, dflt, labels);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting table switch", e);
        }
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        try {
            if (!myHasDecompilingError) {
                mv.visitLookupSwitchInsn(dflt, keys, labels);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting lookup switch", e);
        }
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        try {
            if (!myHasDecompilingError) {
                mv.visitMultiANewArrayInsn(desc, dims);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting multi dimension new array instruction", e);
        }
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        try {
            if (!myHasDecompilingError) {
                mv.visitTryCatchBlock(start, end, handler, type);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting try-catch block", e);
        }
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        try {
            mv.visitLocalVariable(name, desc, signature, start, end, index);
        } catch (RuntimeException e) {
            if (!myHasDecompilingError) {
                processOccurredError("Error occurred while visiting local variable", e);
            }
        }
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        try {
            if (!myHasDecompilingError) {
                mv.visitLineNumber(line, start);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting line number", e);
        }
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        try {
            if (!myHasDecompilingError) {
                mv.visitMaxs(maxStack, maxLocals);
            }
        } catch (RuntimeException e) {
            processOccurredError("Error occurred while visiting maxs", e);
        }
    }

    @Override
    public void visitEnd() {
        try {
            mv.visitEnd();
        } catch (RuntimeException e) {
            if (!myHasDecompilingError) {
                processOccurredError("Error occurred while visiting end", e);
            }
        }
        ((AbstractMethodVisitor) mv).getDecompiledMethod().setError(myError);
    }
}
