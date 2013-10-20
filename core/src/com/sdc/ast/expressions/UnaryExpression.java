package com.sdc.ast.expressions;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;
import com.sdc.languages.general.printers.OperationPrinter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


import static com.sdc.ast.ExpressionType.NOT;

public class UnaryExpression extends PriorityExpression {
    protected final Expression myOperand;

    public UnaryExpression(final @NotNull ExpressionType expressionType, final @NotNull Expression operand) {
        super(expressionType,expressionType == NOT ? Type.BOOLEAN_TYPE : operand.getType());
        this.myOperand = operand;
    }

    @NotNull
    public Expression getOperand() {
        return myOperand;
    }

    @NotNull
    public String getOperation(final @NotNull OperationPrinter operationPrinter) {
        switch (myExpressionType) {
            case NOT:
                return operationPrinter.getNotView();
            case NEGATE:
                return operationPrinter.getNegateView();
            default:
                return "";
        }
    }

    @NotNull
    @Override
    public Expression invert() {
        switch (myExpressionType) {
            case NOT:
                return myOperand;
            default:
                return super.invert();
        }
    }

    @NotNull
    @Override
    public String toString() {
        return "UnaryExpression{" +
                "myExpressionType=" + myExpressionType +
                ", myOperand=" + myOperand +
                '}';
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
