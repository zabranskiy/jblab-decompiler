package com.sdc.ast.expressions;

import com.sdc.ast.OperationType;

public class Constant extends PriorityExpression {
    private final Object myValue;
    private final boolean myIsStringValue;


    public Constant(final Object value, final boolean isStringValue) {
        this(value, isStringValue, false);
    }

    public Constant(final Object value, final boolean isStringValue, final boolean hasDoubleLength) {
        this.myValue = value;
        this.myIsStringValue = isStringValue;
        setDoubleLength(hasDoubleLength);
        myType = OperationType.CONST;
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
                '}';
    }

    public boolean isThis(){
        return myValue.equals("this") && !myIsStringValue;
    }

}
