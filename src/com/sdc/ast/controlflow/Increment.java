package com.sdc.ast.controlflow;

import com.sdc.abstractLanguage.AbstractOperationPrinter;
import com.sdc.ast.OperationType;
import com.sdc.ast.expressions.ExprIncrement;
import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.identifiers.Variable;

public class Increment extends Statement implements ExpressionWrapper {
    private final ExprIncrement myExprIncrement;

    public Increment(Variable v, int increment) {
        myExprIncrement = new ExprIncrement(v, increment);
    }

    public Increment(ExprIncrement exprIncrement) {
        myExprIncrement = exprIncrement;
    }

    public Increment(Variable v, Expression increment, OperationType type) {
        myExprIncrement = new ExprIncrement(v, increment, type);
    }

    public Expression getIncrementExpression() {
        return myExprIncrement.getIncrementExpression();
    }


    public Expression getName() {
        return myExprIncrement.getVariableName();
    }

    public String getOperation(AbstractOperationPrinter operationPrinter) {
       return myExprIncrement.getOperation(operationPrinter);
    }

    public OperationType getOperationType() {
        return myExprIncrement.getOperationType();
    }

    public Variable getVariable() {
        return myExprIncrement.getVariable();
    }

    public boolean IsIncrementSimple() {
        return myExprIncrement.IsIncrementSimple();
    }

    public int getPriority(AbstractOperationPrinter operationPrinter){
        return myExprIncrement.getPriority(operationPrinter);
    }

    @Override
    public Expression toExpression() {
        return myExprIncrement;
    }
}
