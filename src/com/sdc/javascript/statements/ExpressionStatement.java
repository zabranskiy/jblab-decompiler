package com.sdc.javascript.statements;

import com.sdc.javascript.expressions.Expression;

public class ExpressionStatement extends Statement {
    public final Expression expr;

    public ExpressionStatement (final Expression expr) {
        this.expr = expr;
    }
}
