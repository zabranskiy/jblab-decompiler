package com.sdc.ast.expressions;

import static com.sdc.ast.OperationType.NOT;

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

    public Expression invert() {
        return new UnaryExpression(NOT, this);
    }
}
