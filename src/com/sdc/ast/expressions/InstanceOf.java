package com.sdc.ast.expressions;

public class InstanceOf extends Expression {
    private final String myType;
    private final Expression myArgument;
    private boolean myIsInverted = false;

    public InstanceOf(final String type, final Expression argument) {
        this.myType = type;
        this.myArgument = argument;
    }

    public InstanceOf(final String type) {
        this.myType = type;
        this.myArgument = null;
    }

    public String getType() {
        return myType;
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
}
