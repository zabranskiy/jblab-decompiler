package com.sdc.ast.expressions.nestedclasses;

import com.sdc.languages.general.languageParts.GeneralClass;
import com.sdc.languages.general.languageParts.Method;
import com.sdc.ast.Type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class LambdaFunction extends NestedClass {
    public LambdaFunction(final @NotNull GeneralClass nestedClass, final @NotNull String type) {
        super(nestedClass);
        setType(new Type(type));
    }

    @Nullable
    public Method getFunction() {
        final List<Method> methods = myNestedClass.getMethods();
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
