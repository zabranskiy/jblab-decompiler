package com.sdc.ast.expressions;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.expressions.identifiers.Identifier;

public class SquareBrackets extends Identifier {
    private Expression myOperand;
    private Expression myIndex;

    public SquareBrackets(Expression operand, Expression index) {
        super(ExpressionType.SQUARE_BRACKETS, operand.getType().getTypeWithOnPairOfBrackets());
        myOperand = operand;
        myIndex = index;
    }

    public Expression getOperand() {
        return myOperand;
    }

    public Expression getIndex() {
        return myIndex;
    }

    @Override
    public String toString() {
        return "SquareBrackets{" +
                "myOperand=" + myOperand +
                ", myIndex=" + myIndex +
                '}';
    }

    @Override
    public Expression getName() {
        return myOperand;
    }
}
