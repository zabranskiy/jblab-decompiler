package com.sdc.ast.expressions;

public class Constant extends Expression {
    private final Object myValue;

    public Constant(Object value) {
        this.myValue = value;
    }

    public Object getValue() {
        return myValue;
    }
}
