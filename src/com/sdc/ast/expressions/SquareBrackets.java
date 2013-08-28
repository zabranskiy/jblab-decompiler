package com.sdc.ast.expressions;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.expressions.identifiers.Identifier;
import com.sdc.ast.expressions.identifiers.Variable;

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

    @Override
    public boolean findVariable(Variable variable) {
        return myOperand.findVariable(variable) || myIndex.findVariable(variable);
    }
}
