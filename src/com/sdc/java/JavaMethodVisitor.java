package com.sdc.java;

import com.sdc.abstractLanguage.*;
import com.sdc.ast.controlflow.InstanceInvocation;
import com.sdc.ast.controlflow.Invocation;
import com.sdc.ast.expressions.*;
import com.sdc.ast.expressions.identifiers.Variable;
import com.sdc.util.*;

import org.objectweb.asm.util.Printer;

import java.util.*;

public class JavaMethodVisitor extends AbstractMethodVisitor {
    public JavaMethodVisitor(final AbstractMethod abstractMethod, final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName) {
        super(abstractMethod, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);

        this.myLanguagePartFactory = new JavaLanguagePartFactory();
        this.myVisitorFactory = new JavaVisitorFactory();
        this.myLanguage = DeclarationWorker.SupportedLanguage.JAVA;
    }

    @Override
    protected boolean checkForAutomaticallyGeneratedAnnotation(final String annotationName) {
        return false;
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
        String returnType = DeclarationWorker.getJavaDescriptor(desc, returnTypeIndex, myJavaMethod.getImports());

        final String decompiledOwnerClassName = DeclarationWorker.getDecompiledFullClassName(owner);

        String invocationName = "";
        boolean needToRemoveThisFromStack = true;

        if (opString.contains("INVOKEVIRTUAL") || opString.contains("INVOKEINTERFACE")
                || (decompiledOwnerClassName.equals(myDecompiledOwnerFullClassName) && !name.equals("<init>"))) {
            Variable v = (Variable) myBodyStack.pop();
            if (myBodyStack.isEmpty()) {
                myStatements.add(new InstanceInvocation(name, returnType, arguments, v));
            } else {
                myBodyStack.push(new com.sdc.ast.expressions.InstanceInvocation(name, returnType, arguments, v));
            }
            return;
        } else if (opString.contains("INVOKESPECIAL")) {
            if (name.equals("<init>")) {
                myJavaMethod.addImport(decompiledOwnerClassName);
                invocationName = DeclarationWorker.getClassName(owner);
                returnType = invocationName + " ";
            } else {
                invocationName = "super." + name;
            }
        } else if (opString.contains("INVOKESTATIC")) {
            myJavaMethod.addImport(decompiledOwnerClassName);
            invocationName = DeclarationWorker.getClassName(owner) + "." + name;
            needToRemoveThisFromStack = false;
        }

        if (name.equals("<init>")) {
            if (myDecompiledOwnerFullClassName.endsWith(myJavaMethod.getName()) && myDecompiledOwnerSuperClassName.endsWith(invocationName)) {
                removeThisVariableFromStack();
                myStatements.add(new Invocation("super", returnType, arguments));
            } else {
                myBodyStack.push(new New(new com.sdc.ast.expressions.Invocation(invocationName, returnType, arguments)));
            }
        } else {
            if (needToRemoveThisFromStack) {
                removeThisVariableFromStack();
            }

            if (myBodyStack.isEmpty()) {
                myStatements.add(new Invocation(invocationName, returnType, arguments));
            } else {
                myBodyStack.push(new com.sdc.ast.expressions.Invocation(invocationName, returnType, arguments));
            }
        }
    }
}
