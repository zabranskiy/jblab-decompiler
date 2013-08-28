package com.sdc.ast.expressions.nestedclasses;

import com.sdc.languages.general.languageParts.AbstractClass;
import com.sdc.ast.expressions.Expression;

import java.util.List;

public class AnonymousClass extends NestedClass {
    private final List<Expression> myConstructorArguments;

    public AnonymousClass(final AbstractClass anonymousClass, final List<Expression> constructorArguments) {
        super(anonymousClass);
        this.myConstructorArguments = constructorArguments;
    }

    public List<Expression> getConstructorArguments() {
        return myConstructorArguments;
    }
}
