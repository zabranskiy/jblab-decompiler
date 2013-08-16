package com.sdc.ast.controlflow;

import com.sdc.ast.expressions.Expression;

public class New extends Statement {
    private final com.sdc.ast.expressions.New myNewExpression;

    public New(final com.sdc.ast.expressions.New newExpression) {
        this.myNewExpression = newExpression;
    }

    public Expression toExpression() {
        return myNewExpression;
    }
}
