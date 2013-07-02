package com.sdc.javascript.statements;

import com.sdc.javascript.expressions.Expression;

public class Return extends Statement {
    public final Expression value;

    public Return() {
        this.value = null;
    }

    public Return(final Expression argument) {
        this.value = argument;
    }
}
