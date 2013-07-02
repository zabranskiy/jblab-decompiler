package com.sdc.javascript.statements;

import com.sdc.javascript.expressions.Expression;

public class IfStatement extends Statement {
    public final Expression test;
    public final Statement consequent;
    public final Statement alternate;

    public IfStatement (final Expression test, final Statement consequent) {
        this.test = test;
        this.consequent = consequent;
        this.alternate = null;
    }

    public IfStatement (final Expression test, final Statement consequent, final Statement alternate) {
        this.test = test;
        this.consequent = consequent;
        this.alternate = alternate;
    }
}
