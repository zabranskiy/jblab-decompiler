package com.sdc.javascript.statements;

import com.sdc.javascript.expressions.Expression;

public class WhileStatement extends Statement {
    public final Expression test;
    public final Statement body;

    public WhileStatement(final Expression test, final Statement body) {
        this.test = test;
        this.body = body;
    }
}
