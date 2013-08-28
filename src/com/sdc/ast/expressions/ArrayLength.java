package com.sdc.ast.expressions;

import com.sdc.abstractLanguage.AbstractOperationPrinter;
import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;

public class ArrayLength extends PriorityExpression {
    Expression myOperand;

    public ArrayLength(Expression myOperand) {
        super(ExpressionType.ARRAYLENGTH, Type.INT_TYPE);
        this.myOperand = myOperand;
    }

    public Expression getOperand() {
        return myOperand;
    }

    public String getOperation(AbstractOperationPrinter operationPrinter) {
        switch (myExpressionType) {
            case ARRAYLENGTH:
                return operationPrinter.getArrayLengthView();
            default:
                return "";
        }
    }

}
