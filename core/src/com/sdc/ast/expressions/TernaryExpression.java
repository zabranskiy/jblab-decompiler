package com.sdc.ast.expressions;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class TernaryExpression extends PriorityExpression {
    private final Expression myCondition;
    private final Expression myLeft;
    private final Expression myRight;

    public TernaryExpression(final @NotNull Expression myCondition,
                             final @NotNull Expression myLeft,
                             final @NotNull Expression myRight) {
        super(ExpressionType.TERNARY_IF, Type.getStrongerType(myLeft.getType(), myRight.getType()));
        this.myCondition = myCondition;
        this.myLeft = myLeft;
        this.myRight = myRight;
    }

    @NotNull
    public Expression getLeft() {
        return myLeft;
    }

    @NotNull
    public Expression getRight() {
        return myRight;
    }

    @NotNull
    public Expression getCondition() {
        return myCondition;
    }

    @NotNull
    @Override
    public String toString() {
        return "TernaryExpression{" +
                "myCondition=" + myCondition +
                ", myLeft=" + myLeft +
                ", myRight=" + myRight +
                '}';
    }
    @Override
    public boolean findVariable(final @NotNull Variable variable) {
        return myCondition.findVariable(variable) || myLeft.findVariable(variable) || myRight.findVariable(variable);
    }

    @NotNull
    @Override
    public List<Expression> getSubExpressions() {
        final List<Expression> subExpressions = new ArrayList<Expression>();
        subExpressions.add(myLeft);
        subExpressions.add(myRight);
        subExpressions.add(myCondition);
        return subExpressions;
    }
}
