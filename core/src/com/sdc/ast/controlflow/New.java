package com.sdc.ast.controlflow;

import com.sdc.ast.expressions.Expression;

import org.jetbrains.annotations.NotNull;


public class New extends Statement implements ExpressionWrapper {
    private final com.sdc.ast.expressions.New myNewExpression;

    public New(final @NotNull com.sdc.ast.expressions.New newExpression) {
        this.myNewExpression = newExpression;
    }

    @NotNull
    @Override
    public Expression toExpression() {
        return myNewExpression;
    }
}
