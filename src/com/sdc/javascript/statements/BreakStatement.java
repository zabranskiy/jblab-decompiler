package com.sdc.javascript.statements;

import com.sdc.javascript.expressions.Identifier;

public class BreakStatement extends Statement {
    public final Identifier label;

    public BreakStatement() {
        label = null;
    }

    public BreakStatement(final Identifier label) {
        this.label = label;
    }
}
