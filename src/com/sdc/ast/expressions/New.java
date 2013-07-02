package com.sdc.ast.expressions;

public class New extends Expression {
    private final Invocation myConstructor;

    public New(final Invocation constructor) {
        this.myConstructor = constructor;
    }

    public Invocation getConstructor() {
        return myConstructor;
    }

}
