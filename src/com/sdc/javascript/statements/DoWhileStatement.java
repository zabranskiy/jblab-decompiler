package com.sdc.javascript.statements;

import com.sdc.javascript.expressions.Expression;

public class DoWhileStatement extends Statement {
    public final Statement body;
    public final Expression test;

    public DoWhileStatement(final Statement body, final Expression test) {
        this.body = body;
        this.test = test;
    }
}
