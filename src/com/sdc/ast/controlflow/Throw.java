package com.sdc.ast.controlflow;

import com.sdc.ast.expressions.Expression;

public class Throw extends Statement {
    private final Expression myThrowObject;

    public Throw(final Expression throwObject) {
        this.myThrowObject = throwObject;
    }

    public Expression getThrowObject() {
        return myThrowObject;
    }
}
