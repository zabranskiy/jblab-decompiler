package com.sdc.ast.controlflow;

import com.sdc.ast.expressions.Expression;

public class Invocation extends Statement implements ExpressionWrapper {
    private final com.sdc.ast.expressions.Invocation myInvocation;

    public Invocation(final com.sdc.ast.expressions.Invocation invocation) {
        this.myInvocation = invocation;
    }

    @Override
    public Expression toExpression() {
        return myInvocation;
    }
}
