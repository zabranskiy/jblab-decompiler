package com.sdc.ast.expressions.nestedclasses;

import com.sdc.languages.general.languageParts.GeneralClass;
import com.sdc.languages.general.languageParts.Method;
import com.sdc.ast.Type;

import java.util.List;

public class LambdaFunction extends NestedClass {
    //private final String myLambdaFunctionType;

    public LambdaFunction(final GeneralClass nestedClass, final String type) {
        super(nestedClass);
        setType(new Type(type));
    }

    public Method getFunction() {
        List<Method> methods = myNestedClass.getMethods();
        for (final Method method : methods) {
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
