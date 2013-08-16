package com.sdc.ast.controlflow;

import com.sdc.ast.expressions.Expression;

public class New extends Statement implements ExpressionWrapper {
    private final com.sdc.ast.expressions.New myNewExpression;

    public New(final com.sdc.ast.expressions.New newExpression) {
        this.myNewExpression = newExpression;
    }

    @Override
    public Expression toExpression() {
        return myNewExpression;
    }
}
