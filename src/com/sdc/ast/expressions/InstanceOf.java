package com.sdc.ast.expressions;

import com.sdc.ast.OperationType;

public class InstanceOf extends PriorityExpression {
    private final String myInstanceOfType;
    private final Expression myArgument;
    private boolean myIsInverted = false;

    public InstanceOf(final String type, final Expression argument) {
        this.myInstanceOfType = type;
        this.myArgument = argument;
        myType = OperationType.INSTANCEOF;
    }

    public InstanceOf(final String type) {
        this(type, null);
    }

    public String getType() {
        return myInstanceOfType;
    }

    public Expression getArgument() {
        return myArgument;
    }

    @Override
    public Expression invert() {
        myIsInverted = !myIsInverted;
        return this;
    }

    public boolean isInverted() {
        return myIsInverted;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }
}
