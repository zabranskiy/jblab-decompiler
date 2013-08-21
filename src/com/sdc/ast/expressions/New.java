package com.sdc.ast.expressions;

import com.sdc.ast.OperationType;

public class New extends PriorityExpression {
    private final Invocation myConstructor;

    public New(final Invocation constructor) {
        this.myConstructor = constructor;
        setDoubleLength(false);
        myType = OperationType.NEW;
    }

    public Invocation getConstructor() {
        return myConstructor;
    }

    public String getReturnType() {
        return myConstructor.getReturnType();
    }

    @Override
    public boolean isBoolean() {
        return false;
    }
}
