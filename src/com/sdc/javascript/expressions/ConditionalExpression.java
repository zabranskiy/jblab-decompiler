package com.sdc.javascript.expressions;

public class ConditionalExpression extends Expression {
    public final Expression test;
    public final Expression consequent;
    public final Expression alternate;

    public ConditionalExpression(final Expression test, final Expression consequent, final Expression alternate) {
        this.test = test;
        this.consequent = consequent;
        this.alternate = alternate;
    }

}
