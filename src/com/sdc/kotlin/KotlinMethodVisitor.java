package com.sdc.kotlin;

import com.sdc.abstractLanguage.AbstractClassVisitor;
import com.sdc.abstractLanguage.AbstractMethod;
import com.sdc.abstractLanguage.AbstractMethodVisitor;
import com.sdc.ast.expressions.*;
import com.sdc.ast.expressions.identifiers.Variable;
import com.sdc.ast.expressions.nestedclasses.LambdaFunction;
import com.sdc.util.DeclarationWorker;

import org.objectweb.asm.*;
import org.objectweb.asm.util.Printer;

import java.io.IOException;
import java.util.*;

public class KotlinMethodVisitor extends AbstractMethodVisitor {
    public KotlinMethodVisitor(final AbstractMethod abstractMethod, final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName) {
        super(abstractMethod, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);

        this.myLanguagePartFactory = new KotlinLanguagePartFactory();
        this.myVisitorFactory = new KotlinVisitorFactory();
        this.myLanguage = DeclarationWorker.SupportedLanguage.KOTLIN;
    }

    @Override
    protected boolean checkForAutomaticallyGeneratedAnnotation(String annotationName) {
        return annotationName.startsWith("Jet");
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
        super.visitFieldInsn(opcode, owner, name, desc);

        final String opString = Printer.OPCODES[opcode];

        if (opString.contains("GETSTATIC")) {
            final String decompiledOwnerName = DeclarationWorker.getDecompiledFullClassName(owner);
            final int srcIndex = myDecompiledOwnerFullClassName.indexOf("$src$");
            final String methodOwner = srcIndex == -1 ? myDecompiledOwnerFullClassName : myDecompiledOwnerFullClassName.substring(0, srcIndex);
            if (name.equals("instance$") && decompiledOwnerName.contains(methodOwner) && decompiledOwnerName.contains(myDecompiledMethod.getName())) {
                try {
                    AbstractClassVisitor cv = new KotlinClassVisitor(myDecompiledMethod.getTextWidth(), myDecompiledMethod.getNestSize());
                    ClassReader cr = new ClassReader(decompiledOwnerName);
                    cr.accept(cv, 0);
                    myBodyStack.push(new LambdaFunction(cv.getDecompiledClass());
                } catch (IOException e) {
                }
            }
        }
    }

    @Override
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc) {
        final String opString = Printer.OPCODES[opcode];
        //System.out.println(opString + " " + owner + " " + name + " " + desc);

        List<Expression> arguments = new ArrayList<Expression>();
        for (int i = 0; i < DeclarationWorker.getParametersCount(desc); i++) {
            arguments.add(0, getTopOfBodyStack());
        }

        final int returnTypeIndex = desc.indexOf(')') + 1;
        String returnType = DeclarationWorker.getKotlinDescriptor(desc, returnTypeIndex, myKotlinMethod.getImports());

        final String decompiledOwnerClassName = DeclarationWorker.getDecompiledFullClassName(owner);

        String invocationName = "";
        boolean needToRemoveThisFromStack = true;

        if (opString.contains("INVOKEVIRTUAL") || opString.contains("INVOKEINTERFACE")) {
            if (!name.equals("<init>")) {
                 if (!myBodyStack.isEmpty() && myBodyStack.peek() instanceof Variable) {
                    Variable v = (Variable) myBodyStack.pop();
                    if (myBodyStack.isEmpty()) {
                        myStatements.add(new com.sdc.ast.controlflow.InstanceInvocation(name, returnType, arguments, v));
                    } else {
                        myBodyStack.push(new com.sdc.ast.expressions.InstanceInvocation(name, returnType, arguments, v));
                    }
                    return;
                } else {
                    invocationName = "." + name;
                }
            }
        } else if (opString.contains("INVOKESPECIAL")) {
            if (name.equals("<init>")) {
                final String decompiledOwnerName = DeclarationWorker.getDecompiledFullClassName(owner);
                final int srcIndex = myDecompiledOwnerFullClassName.indexOf("$src$");
                final String methodOwner = srcIndex == -1 ? myDecompiledOwnerFullClassName : myDecompiledOwnerFullClassName.substring(0, srcIndex);
                if (decompiledOwnerName.contains(methodOwner) && decompiledOwnerName.contains(myKotlinMethod.getName())) {
                    try {
                        AbstractClassVisitor cv = new KotlinClassVisitor(myKotlinMethod.getTextWidth(), myKotlinMethod.getNestSize());
                        ClassReader cr = new ClassReader(decompiledOwnerName);
                        cr.accept(cv, 0);
                        LambdaFunction lf = new LambdaFunction(((KotlinClassVisitor) cv).getDecompiledClass());
                        if (lf.isKotlinLambda()) {
                            myBodyStack.push(lf);
                            return;
                        }
                    } catch (IOException e) {

                    }
                }

                myKotlinMethod.addImport(decompiledOwnerClassName);
                invocationName = DeclarationWorker.getClassName(owner);
                returnType = invocationName + " ";
            } else {
                invocationName = "super<" + DeclarationWorker.getClassName(owner) + ">."  + name;
            }
        } else if (opString.contains("INVOKESTATIC")) {
            myKotlinMethod.addImport(decompiledOwnerClassName);
            final String ownerClassName = DeclarationWorker.getClassName(owner);
            if (!ownerClassName.equals("KotlinPackage")) {
                if (!ownerClassName.contains("$src$")) {
                    invocationName = ownerClassName + "." + name;
                } else {
                    invocationName = name;
                }
            } else {
                Variable variable = (Variable) arguments.remove(0);
                if (myBodyStack.isEmpty()) {
                    myStatements.add(new com.sdc.ast.controlflow.InstanceInvocation(name, returnType, arguments, variable));
                } else {
                    myBodyStack.push(new com.sdc.ast.expressions.InstanceInvocation(name, returnType, arguments, variable));
                }
                return;
            }
            needToRemoveThisFromStack = false;
            if (name.equals("checkParameterIsNotNull")) {
                ((KotlinFrame) getCurrentFrame()).addNotNullVariable(((Variable) arguments.get(0)).getIndex());
                return;
            }
        }

        if (name.equals("<init>")) {
            if (myDecompiledOwnerFullClassName.endsWith(myKotlinMethod.getName()) && myDecompiledOwnerSuperClassName.endsWith(invocationName)) {
                removeThisVariableFromStack();
                myKotlinMethod.getKotlinClass().setSuperClassConstructor(new com.sdc.ast.expressions.Invocation(invocationName, returnType, arguments));
            } else {
                myBodyStack.push(new New(new com.sdc.ast.expressions.Invocation(invocationName, returnType, arguments)));
            }
        } else {
            if (needToRemoveThisFromStack) {
                removeThisVariableFromStack();
            }

            if (myBodyStack.isEmpty()) {
                myStatements.add(new com.sdc.ast.controlflow.Invocation(invocationName, returnType, arguments));
            } else {
                myBodyStack.push(new com.sdc.ast.expressions.Invocation(invocationName, returnType, arguments));
            }
        }
    }
}

