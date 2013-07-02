package com.sdc.javascript.statements;

import com.sdc.javascript.expressions.Expression;

public class Throw extends Statement {
    public final Expression expr;

    public Throw(final Expression expr) {
        this.expr = expr;
    }
}
