package com.sdc.ast.expressions.nestedclasses;

import com.sdc.languages.general.languageParts.GeneralClass;
import com.sdc.ast.expressions.Expression;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class AnonymousClass extends NestedClass {
    private final List<Expression> myConstructorArguments;

    public AnonymousClass(final @NotNull GeneralClass anonymousClass, final @NotNull List<Expression> constructorArguments) {
        super(anonymousClass);
        this.myConstructorArguments = constructorArguments;
    }

    @NotNull
    public List<Expression> getConstructorArguments() {
        return myConstructorArguments;
    }

    @NotNull
    @Override
    public List<Expression> getSubExpressions() {
        final List<Expression> subExpressions = new ArrayList<Expression>();
        subExpressions.addAll(myConstructorArguments);
        return subExpressions;
    }
}
