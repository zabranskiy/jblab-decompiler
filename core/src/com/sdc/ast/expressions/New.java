package com.sdc.ast.expressions;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;

import org.jetbrains.annotations.NotNull;


public class New extends PriorityExpression {
    private final Invocation myConstructor;

    public New(final @NotNull Invocation constructor) {
        super(ExpressionType.NEW, new Type(constructor.getFunction()));
        this.myConstructor = constructor;
    }

    @NotNull
    public Invocation getConstructor() {
        return myConstructor;
    }

    @Override
    public boolean findVariable(final @NotNull Variable variable) {
        return myConstructor.findVariable(variable);
    }
}
