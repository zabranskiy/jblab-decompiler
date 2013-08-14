package com.sdc.ast.expressions;

import com.sdc.abstractLanguage.AbstractOperationPrinter;
import com.sdc.ast.OperationType;

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
}
