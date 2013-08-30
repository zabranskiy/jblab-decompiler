package com.sdc.ast.expressions;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;
import com.sdc.languages.general.printers.OperationPrinter;

import java.util.ArrayList;
import java.util.List;

import static com.sdc.ast.ExpressionType.NOT;

public class UnaryExpression extends PriorityExpression {
    protected final Expression myOperand;

    public UnaryExpression(final ExpressionType expressionType, final Expression operand) {
        super(expressionType,expressionType == NOT ? Type.BOOLEAN_TYPE : operand.getType());
        this.myOperand = operand;
    }

    public Expression getOperand() {
        return myOperand;
    }

    public String getOperation(OperationPrinter operationPrinter) {
        switch (myExpressionType) {
            case NOT:
                return operationPrinter.getNotView();
            case NEGATE:
                return operationPrinter.getNegateView();
            default:
                return "";
        }
    }

    @Override
    public Expression invert() {
        switch (myExpressionType) {
            case NOT:
                return myOperand;
            default:
                return super.invert();
        }
    }

    @Override
    public String toString() {
        return "UnaryExpression{" +
                "myExpressionType=" + myExpressionType +
                ", myOperand=" + myOperand +
                '}';
    }

    @Override
      public boolean findVariable(Variable variable) {
        return myOperand.findVariable(variable);
    }

    @Override
    public List<Expression> getSubExpressions() {
        List<Expression> subExpressions = new ArrayList<Expression>();
        subExpressions.add(myOperand);
        return subExpressions;
    }
}
