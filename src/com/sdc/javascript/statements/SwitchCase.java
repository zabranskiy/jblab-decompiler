package com.sdc.javascript.statements;

import com.sdc.javascript.expressions.Expression;

import java.util.List;

public class SwitchCase {
    public final Expression test;
    public final List<Statement> consquent;

    public SwitchCase(final List<Statement> consquent) {
        this.test = null;
        this.consquent = consquent;
    }

    public SwitchCase(final Expression test, final List<Statement> consquent) {
        this.test = test;
        this.consquent = consquent;
    }

}
