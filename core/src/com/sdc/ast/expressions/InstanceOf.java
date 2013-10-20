package com.sdc.ast.expressions;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class InstanceOf extends PriorityExpression {
    private final Type myInstanceOfType;
    private final Expression myArgument;
    private boolean myIsInverted = false;

    public InstanceOf(final @NotNull Type instanceOfType, final @Nullable Expression argument) {
        super(ExpressionType.INSTANCEOF, Type.BOOLEAN_TYPE);
        this.myInstanceOfType = instanceOfType;
        this.myArgument = argument;
    }

    public InstanceOf(final @NotNull Type instanceOfType) {
        this(instanceOfType, null);
    }

    @NotNull
    public Type getInstanceOfType() {
        return myInstanceOfType;
    }

    public Expression getArgument() {
        return myArgument;
    }

    @NotNull
    @Override
    public Expression invert() {
        myIsInverted = !myIsInverted;
        return this;
    }

    public boolean isInverted() {
        return myIsInverted;
    }

    @Override
    public boolean findVariable(final @NotNull Variable variable) {
        return myArgument != null && myArgument.findVariable(variable);
    }

    @NotNull
    @Override
    public List<Expression> getSubExpressions() {
        final List<Expression> subExpressions = new ArrayList<Expression>();
        subExpressions.add(myArgument);
        return subExpressions;
    }
}
