package com.sdc.ast.expressions;

public class Constant extends Expression {
    private final Object myValue;
    private final boolean myIsStringValue;

    public Constant(final Object value, final boolean isStringValue) {
        this.myValue = value;
        this.myIsStringValue = isStringValue;
    }

    public Object getValue() {
        return myValue;
    }

    public boolean isStringValue() {
        return myIsStringValue;
    }
}
