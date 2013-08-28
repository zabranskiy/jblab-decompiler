package com.sdc.ast.expressions.nestedclasses;

import com.sdc.abstractLanguage.AbstractClass;
import com.sdc.abstractLanguage.AbstractMethod;
import com.sdc.ast.Type;

import java.util.List;

public class LambdaFunction extends NestedClass {
    //private final String myLambdaFunctionType;

    public LambdaFunction(final AbstractClass nestedClass, final String type) {
        super(nestedClass);
        setType(new Type(type));
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

}
