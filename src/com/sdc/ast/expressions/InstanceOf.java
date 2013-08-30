package com.sdc.ast.expressions;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;

import java.util.ArrayList;
import java.util.List;

public class InstanceOf extends PriorityExpression {
    private final Type myInstanceOfType;
    private final Expression myArgument;
    private boolean myIsInverted = false;

    public InstanceOf(final Type instanceOfType, final Expression argument) {
        super(ExpressionType.INSTANCEOF, Type.BOOLEAN_TYPE);
        this.myInstanceOfType = instanceOfType;
        this.myArgument = argument;
    }

    public InstanceOf(final Type instanceOfType) {
        this(instanceOfType, null);
    }

    public Type getInstanceOfType() {
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
    public boolean findVariable(Variable variable) {
        return myArgument.findVariable(variable);
    }

    @Override
    public List<Expression> getSubExpressions() {
        List<Expression> subExpressions = new ArrayList<Expression>();
        subExpressions.add(myArgument);
        return subExpressions;
    }
}
