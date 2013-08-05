package com.sdc.ast.expressions.nestedclasses;

import com.sdc.abstractLanguage.AbstractClass;
import com.sdc.abstractLanguage.AbstractMethod;

import java.util.List;

public class LambdaFunction extends NestedClass {
    private final String myType;

    public LambdaFunction(final AbstractClass nestedClass, final String type) {
        super(nestedClass);
        this.myType = type;
    }

    public AbstractMethod getFunction() {
        List<AbstractMethod> methods = myNestedClass.getMethods();
        for (final AbstractMethod method : methods) {
            if (!method.getModifier().contains("bridge") && method.getName().equals("invoke")) {
                return method;
            }
        }
        return null;
    }

    public boolean isKotlinLambda() {
        final String superClass = myNestedClass.getSuperClass();
        return superClass.startsWith("FunctionImpl");
    }

    public String getType() {
        return myType;
    }
}
