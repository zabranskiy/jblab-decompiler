package com.sdc.javascript.expressions;

public class Assignment extends Expression {
    public final Identifier left;
    public final Expression right;

    public Assignment(final Identifier left, final Expression right) {
        this.left = left;
        this.right = right;
    }
}
