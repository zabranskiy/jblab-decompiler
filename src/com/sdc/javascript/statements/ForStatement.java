package com.sdc.javascript.statements;

import com.sdc.javascript.expressions.Expression;

public class ForStatement extends Statement {
    public final Expression init;
    public final Expression test;
    public final Expression update;
    public final Statement body;

    public ForStatement(final Expression init, final Expression test, final Expression update, final Statement body) {
        this.init = init;
        this.test = test;
        this.update = update;
        this.body = body;
    }
}
