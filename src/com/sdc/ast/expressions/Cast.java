package com.sdc.ast.expressions;

import com.sdc.languages.general.printers.AbstractOperationPrinter;
import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;

import static com.sdc.ast.Type.*;

public class Cast extends PriorityExpression {
    protected Expression myOperand;
    private String myParam = "";     // for CHECK_CAST


    public Cast(final ExpressionType expressionType, final Expression operand) {
        this(expressionType, operand, "");
    }

    public Cast(final ExpressionType expressionType, final Expression operand, String param) {
        super(expressionType, getMyType(expressionType, param));
        this.myOperand = operand;
        this.myParam = param;
    }


    public String getOperation(AbstractOperationPrinter operationPrinter) {
        switch (myExpressionType) {
            case DOUBLE_CAST:
                return operationPrinter.getDoubleCastView();
            case INT_CAST:
                return operationPrinter.getIntCastView();
            case LONG_CAST:
                return operationPrinter.getLongCastView();
            case SHORT_CAST:
                return operationPrinter.getShortCastView();
            case BYTE_CAST:
                return operationPrinter.getByteCastView();
            case FLOAT_CAST:
                return operationPrinter.getFloatCastView();
            case CHAR_CAST:
                return operationPrinter.getCharCastView();
            case CHECK_CAST:
                return operationPrinter.getCheckCast(myParam);
            default:
                return "";
        }
    }

    public Expression getOperand() {
        return myOperand;
    }

    @Override
    public Expression getBase() {
        return myOperand.getBase();
    }

    @Override
    public boolean findVariable(Variable variable) {
        return myOperand.findVariable(variable);
    }

    public static Type getMyType(ExpressionType expressionType, String param) {
        switch (expressionType) {
            case DOUBLE_CAST:
                return DOUBLE_TYPE;
            case INT_CAST:
                return INT_TYPE;
            case LONG_CAST:
                return LONG_TYPE;
            case SHORT_CAST:
                return SHORT_TYPE;
            case BYTE_CAST:
                return BYTE_TYPE;
            case FLOAT_CAST:
                return FLOAT_TYPE;
            case CHAR_CAST:
                return CHAR_TYPE;
            default:
                return new Type(param);
        }
    }
}
