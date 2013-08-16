package com.sdc.ast.expressions;

import com.sdc.abstractLanguage.AbstractOperationPrinter;
import com.sdc.ast.OperationType;

import static com.sdc.ast.OperationType.*;
import static com.sdc.ast.OperationType.EQ;
import static com.sdc.ast.OperationType.NE;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitrii.Pozdin
 * Date: 8/13/13
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class PriorityExpression extends Expression {
    protected OperationType myType;
    public int getPriority(AbstractOperationPrinter operationPrinter){
         return operationPrinter.getPriority(myType);
    }

    public OperationType getOperationType() {
        return myType;
    }

    public void setType(final OperationType type) {
        this.myType = type;
    }

    public boolean isAssociative() {
        if (myType == ADD || myType == MUL || myType == AND || myType == OR ||
                myType == BITWISE_AND || myType == BITWISE_OR || myType == BITWISE_XOR || myType==EQ || myType==NE) {
            return true;
        }
        return false;
    }
}
