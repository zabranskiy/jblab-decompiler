package com.sdc.ast.controlflow;

import com.sdc.languages.general.printers.AbstractOperationPrinter;
import com.sdc.ast.ExpressionType;
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

    public Increment(Variable v, Expression increment, ExpressionType type) {
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

    public ExpressionType getOperationType() {
        return myExprIncrement.getExpressionType();
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
