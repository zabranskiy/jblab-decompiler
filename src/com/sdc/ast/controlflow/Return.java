package com.sdc.ast.controlflow;

import com.sdc.ast.expressions.Expression;

public class Return extends Statement {
    private final Expression myReturnValue;

    public Return() {
        myReturnValue = null;
    }

    public Return(final Expression returnValue) {
        this.myReturnValue = returnValue;
    }

    public Expression getReturnValue() {
        return myReturnValue;
    }
}
