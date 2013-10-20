package com.sdc.ast.expressions;

import com.sdc.languages.general.printers.OperationPrinter;
import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ArrayLength extends PriorityExpression {
    private Expression myOperand;

    public ArrayLength(final @NotNull Expression myOperand) {
        super(ExpressionType.ARRAYLENGTH, Type.INT_TYPE);
        this.myOperand = myOperand;
    }

    @NotNull
    public Expression getOperand() {
        return myOperand;
    }

    @NotNull
    public String getOperation(final @NotNull OperationPrinter operationPrinter) {
        switch (myExpressionType) {
            case ARRAYLENGTH:
                return operationPrinter.getArrayLengthView();
            default:
                return "";
        }
    }

    @Override
    public boolean findVariable(final @NotNull Variable variable) {
        return myOperand.findVariable(variable);
    }

    @NotNull
    @Override
    public List<Expression> getSubExpressions() {
        final List<Expression> subExpressions = new ArrayList<Expression>();
        subExpressions.add(myOperand);
        return subExpressions;
    }
}
