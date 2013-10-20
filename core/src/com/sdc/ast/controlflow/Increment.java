package com.sdc.ast.controlflow;

import com.sdc.languages.general.printers.OperationPrinter;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.expressions.ExprIncrement;
import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.identifiers.Variable;

import org.jetbrains.annotations.NotNull;


public class Increment extends Statement implements ExpressionWrapper {
    private final ExprIncrement myExprIncrement;

    public Increment(final @NotNull Variable v, final int increment) {
        myExprIncrement = new ExprIncrement(v, increment);
    }

    public Increment(final @NotNull ExprIncrement exprIncrement) {
        myExprIncrement = exprIncrement;
    }

    public Increment(final @NotNull Variable v, final @NotNull Expression increment, final @NotNull ExpressionType type) {
        myExprIncrement = new ExprIncrement(v, increment, type);
    }

    @NotNull
    public Expression getIncrementExpression() {
        return myExprIncrement.getIncrementExpression();
    }

    @NotNull
    public Expression getName() {
        return myExprIncrement.getVariableName();
    }

    @NotNull
    public String getOperation(final @NotNull OperationPrinter operationPrinter) {
        return myExprIncrement.getOperation(operationPrinter);
    }

    @NotNull
    public ExpressionType getOperationType() {
        return myExprIncrement.getExpressionType();
    }

    @NotNull
    public Variable getVariable() {
        return myExprIncrement.getVariable();
    }

    public boolean isIncrementSimple() {
        return myExprIncrement.isIncrementSimple();
    }

    public int getPriority(final @NotNull OperationPrinter operationPrinter) {
        return myExprIncrement.getPriority(operationPrinter);
    }

    @NotNull
    @Override
    public Expression toExpression() {
        return myExprIncrement;
    }
}
