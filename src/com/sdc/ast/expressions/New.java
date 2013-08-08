package com.sdc.ast.expressions;

public class New extends Expression {
    private final Invocation myConstructor;

    public New(final Invocation constructor) {
        this.myConstructor = constructor;
        setDoubleLength(false);
    }

    public Invocation getConstructor() {
        return myConstructor;
    }

    public String getReturnType() {
        return myConstructor.getReturnType();
    }
}
