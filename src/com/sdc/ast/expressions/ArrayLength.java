package com.sdc.ast.expressions;

import com.sdc.abstractLanguage.AbstractOperationPrinter;
import com.sdc.ast.OperationType;

public class ArrayLength extends PriorityExpression {
    Expression myOperand;

    public ArrayLength(Expression myOperand) {
        this.myOperand = myOperand;
        myType = OperationType.ARRAYLENGTH;
    }

    public Expression getOperand() {
        return myOperand;
    }

    public String getOperation(AbstractOperationPrinter operationPrinter) {
        switch (myType) {
            case ARRAYLENGTH:
                return operationPrinter.getArrayLengthView();
            default:
                return "";
        }
    }

    @Override
    public boolean isBoolean() {
        return false;
    }
}
