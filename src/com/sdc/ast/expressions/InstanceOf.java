package com.sdc.ast.expressions;

import com.sdc.ast.OperationType;

public class InstanceOf extends PriorityExpression {
    private final String myInstanceOfType;
    private final Expression myArgument;

    public InstanceOf(final String type, final Expression argument) {
        this.myInstanceOfType = type;
        this.myArgument = argument;
        myType = OperationType.INSTANCEOF;
    }

    public String getType() {
        return myInstanceOfType;
    }

    public Expression getArgument() {
        return myArgument;
    }
}
