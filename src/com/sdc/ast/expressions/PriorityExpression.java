package com.sdc.ast.expressions;

import com.sdc.languages.general.printers.AbstractOperationPrinter;
import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;

import static com.sdc.ast.ExpressionType.*;

public abstract class PriorityExpression extends Expression {
    protected final ExpressionType myExpressionType;

    protected PriorityExpression(ExpressionType expressionType, Type myType) {
        super(myType);
        myExpressionType = expressionType;
    }

    public int getPriority(AbstractOperationPrinter operationPrinter) {
        return operationPrinter.getPriority(myExpressionType);
    }

    public ExpressionType getExpressionType() {
        return myExpressionType;
    }


    public boolean isAssociative() {
        if (myExpressionType == ADD || myExpressionType == MUL || myExpressionType == AND || myExpressionType == OR
                || myExpressionType == SQUARE_BRACKETS || myExpressionType == ARRAYLENGTH ||
                myExpressionType == BITWISE_AND || myExpressionType == BITWISE_OR || myExpressionType == BITWISE_XOR || myExpressionType == EQ || myExpressionType == NE ||
                isComplexIncrement() || myExpressionType == INVOCATION
                ) {
            return true;
        }
        return false;
    }

    private boolean isComplexIncrement() {
        return myExpressionType == BITWISE_OR_INC || myExpressionType == BITWISE_AND_INC || myExpressionType == BITWISE_XOR_INC ||
                myExpressionType == SHL_INC || myExpressionType == SHR_INC || myExpressionType == USHR_INC ||
                myExpressionType == ADD_INC || myExpressionType == MUL_INC || myExpressionType == DIV_INC || myExpressionType == SUB_INC || myExpressionType == REM_INC;
    }
}
