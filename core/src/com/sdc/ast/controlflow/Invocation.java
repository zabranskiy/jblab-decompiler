package com.sdc.ast.controlflow;

import com.sdc.ast.expressions.Expression;

import org.jetbrains.annotations.NotNull;


public class Invocation extends Statement implements ExpressionWrapper {
    private final com.sdc.ast.expressions.Invocation myInvocation;

    public Invocation(final @NotNull com.sdc.ast.expressions.Invocation invocation) {
        this.myInvocation = invocation;
    }

    @NotNull
    @Override
    public Expression toExpression() {
        return myInvocation;
    }
}
