package com.sdc.ast.expressions;

import com.sdc.languages.general.printers.OperationPrinter;
import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;

import org.jetbrains.annotations.NotNull;

import static com.sdc.ast.ExpressionType.*;


public abstract class PriorityExpression extends Expression {
    protected final ExpressionType myExpressionType;

    protected PriorityExpression(final @NotNull ExpressionType expressionType, final @NotNull Type myType) {
        super(myType);
        myExpressionType = expressionType;
    }

    public int getPriority(final @NotNull OperationPrinter operationPrinter) {
        return operationPrinter.getPriority(myExpressionType);
    }

    @NotNull
    public ExpressionType getExpressionType() {
        return myExpressionType;
    }

    public boolean isAssociative() {
        return myExpressionType == ADD || myExpressionType == MUL || myExpressionType == AND || myExpressionType == OR
                || myExpressionType == SQUARE_BRACKETS || myExpressionType == ARRAYLENGTH
                || myExpressionType == BITWISE_AND || myExpressionType == BITWISE_OR || myExpressionType == BITWISE_XOR
                || myExpressionType == EQ || myExpressionType == NE
                || isComplexIncrement() || myExpressionType == INVOCATION;
    }

    private boolean isComplexIncrement() {
        return myExpressionType == BITWISE_OR_INC || myExpressionType == BITWISE_AND_INC || myExpressionType == BITWISE_XOR_INC
                || myExpressionType == SHL_INC || myExpressionType == SHR_INC || myExpressionType == USHR_INC
                || myExpressionType == ADD_INC || myExpressionType == MUL_INC || myExpressionType == DIV_INC
                || myExpressionType == SUB_INC || myExpressionType == REM_INC;
    }
}
