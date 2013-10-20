package com.sdc.ast.expressions;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.expressions.identifiers.Identifier;
import com.sdc.ast.expressions.identifiers.Variable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class SquareBrackets extends Identifier {
    private final Expression myOperand;
    private final Expression myIndex;

    public SquareBrackets(final @NotNull Expression operand, final @NotNull Expression index) {
        super(ExpressionType.SQUARE_BRACKETS, operand.getType().getTypeWithOnPairOfBrackets());
        myOperand = operand;
        myIndex = index;
    }

    @NotNull
    public Expression getOperand() {
        return myOperand;
    }

    @NotNull
    public Expression getIndex() {
        return myIndex;
    }

    @NotNull
    @Override
    public String toString() {
        return "SquareBrackets{" + "myOperand=" + myOperand + ", myIndex=" + myIndex + '}';
    }

    @NotNull
    @Override
    public Expression getName() {
        return myOperand;
    }

    @Override
    public boolean findVariable(final @NotNull Variable variable) {
        return myOperand.findVariable(variable) || myIndex.findVariable(variable);
    }

    @NotNull
    @Override
    public List<Expression> getSubExpressions() {
        final List<Expression> subExpressions = new ArrayList<Expression>();
        subExpressions.add(myOperand);
        subExpressions.add(myIndex);
        return subExpressions;
    }
}
