package com.sdc.ast.expressions;

public class Constant extends Expression {
    private final Object myValue;
    private final boolean myIsStringValue;


    public Constant(final Object value, final boolean isStringValue) {
        this.myValue = value;
        this.myIsStringValue = isStringValue;
        setDoubleLength(false);
    }

    public Constant(final Object value, final boolean isStringValue, final boolean hasDoubleLength) {
        this.myValue = value;
        this.myIsStringValue = isStringValue;
        setDoubleLength(hasDoubleLength);
    }
    public Object getValue() {
        return myValue;
    }

    public boolean isStringValue() {
        return myIsStringValue;
    }

    @Override
    public String toString() {
        return "Constant{" +
                "myValue=" + myValue +
               // ", myIsStringValue=" + myIsStringValue +
                '}';
    }


}
