package com.sdc.javascript.statements;

public class LabeledStatement extends Statement {
    public final String label;
    public final Statement body;

    public LabeledStatement(final String label, final Statement body) {
        this.label = label;
        this.body = body;
    }
}
