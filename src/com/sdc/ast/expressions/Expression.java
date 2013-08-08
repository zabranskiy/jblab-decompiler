package com.sdc.ast.expressions;

public abstract class Expression {
    // true for category 2 (double or long types)
    //false for category 1 (other types)
    private boolean hasDoubleLength = false;

    public final boolean hasDoubleLength() {
        return hasDoubleLength;
    }

    public final void setDoubleLength(boolean hasDoubleLength) {
        this.hasDoubleLength = hasDoubleLength;
    }
}
