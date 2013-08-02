package com.sdc.ast.expressions;

public class InstanceOf extends Expression {
    private final String myType;
    private final Expression myArgument;

    public InstanceOf(final String type, final Expression argument) {
        this.myType = type;
        this.myArgument = argument;
    }

    public String getType() {
        return myType;
    }

    public Expression getArgument() {
        return myArgument;
    }
}
