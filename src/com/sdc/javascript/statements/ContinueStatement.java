package com.sdc.javascript.statements;

import com.sdc.javascript.expressions.Identifier;

public class ContinueStatement extends Statement {
    public final Identifier label;

    public ContinueStatement() {
        label = null;
    }

    public ContinueStatement(final Identifier label) {
        this.label = label;
    }
}
