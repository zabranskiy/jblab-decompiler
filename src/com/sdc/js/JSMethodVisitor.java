package com.sdc.js;

import com.sdc.abstractLanguage.AbstractMethod;
import com.sdc.abstractLanguage.AbstractMethodVisitor;
import com.sdc.ast.controlflow.InstanceInvocation;
import com.sdc.ast.controlflow.Invocation;
import com.sdc.ast.expressions.*;
import com.sdc.ast.expressions.identifiers.Variable;
import com.sdc.util.DeclarationWorker;
import org.objectweb.asm.util.Printer;

import java.util.*;

public class JSMethodVisitor extends AbstractMethodVisitor {
    public JSMethodVisitor(final AbstractMethod abstractMethod, final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName) {
        super(abstractMethod, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);

        this.myLanguagePartFactory = new JSLanguagePartFactory();
        this.myVisitorFactory = new JSVisitorFactory();
        this.myLanguage = DeclarationWorker.SupportedLanguage.JAVASCRIPT;
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
        for (int i = 1; i < desc.indexOf(')'); i++) {
            arguments.add(0, getTopOfBodyStack());
        }

        final int returnTypeIndex = desc.indexOf(')') + 1;
        String returnType = DeclarationWorker.getJavaDescriptor(desc, returnTypeIndex, myJavaClassMethod.getImports());

        final String decompiledOwnerClassName = getDecompiledFullClassName(owner);

        String invocationName = "";

        if (opString.contains("INVOKEVIRTUAL") || opString.contains("INVOKEINTERFACE")
                || (decompiledOwnerClassName.equals(myDecompiledOwnerFullClassName) && !name.equals("<init>"))) {
            if (!myBodyStack.isEmpty() && myBodyStack.peek() instanceof Variable) {
                Variable v = (Variable) myBodyStack.pop();
                if (myBodyStack.isEmpty()) {
                    myStatements.add(new InstanceInvocation(name, returnType, arguments, v));
                } else {
                    myBodyStack.push(new com.sdc.ast.expressions.InstanceInvocation(name, returnType, arguments, v));
                }
                return;
            } else {
                invocationName = name;
            }
        } else if (opString.contains("INVOKESPECIAL")) {
            if (name.equals("<init>")) {
                myJavaClassMethod.addImport(decompiledOwnerClassName);
                invocationName = getClassName(owner);
            } else {
                invocationName = "super." + name;
            }
        } else if (opString.contains("INVOKESTATIC")) {
            myJavaClassMethod.addImport(decompiledOwnerClassName);
            invocationName = getClassName(owner) + "." + name;
        }

        if (name.equals("<init>")) {
            myBodyStack.push(new New(new com.sdc.ast.expressions.Invocation(invocationName, returnType, arguments)));
        } else if (myBodyStack.isEmpty()) {
            myStatements.add(new Invocation(invocationName, returnType, arguments));
        } else {
            myBodyStack.push(new com.sdc.ast.expressions.Invocation(invocationName, returnType, arguments));
        }
    }
}
