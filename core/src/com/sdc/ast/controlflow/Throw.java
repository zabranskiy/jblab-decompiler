package com.sdc.ast.controlflow;

import com.sdc.ast.expressions.Expression;

import org.jetbrains.annotations.NotNull;


public class Throw extends Statement {
    private final Expression myThrowObject;

    public Throw(final @NotNull Expression throwObject) {
        this.myThrowObject = throwObject;
    }

    @NotNull
    public Expression getThrowObject() {
        return myThrowObject;
    }
}
