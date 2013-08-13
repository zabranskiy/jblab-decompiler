package com.sdc.ast.expressions;

import com.sdc.abstractLanguage.AbstractOperationPrinter;
import com.sdc.ast.OperationType;

import static com.sdc.ast.OperationType.*;

public class UnaryExpression extends Expression {


    private final Expression myOperand;
    private OperationType myType;
    private String myParam="";     // for CHECK_CAST

    public UnaryExpression(final OperationType type, final Expression operand) {
        this.myType = type;
        this.myOperand = operand;
        if (myType == NOT || myType == NEGATE) {
            setDoubleLength(operand.hasDoubleLength());
        } else {
            setDoubleLength(myType == DOUBLE_CAST || myType == LONG_CAST);
        }
    }

    public UnaryExpression(final OperationType type, final Expression operand, String myParam) {
        this(type,operand);
        this.myParam = myParam;
    }

    public UnaryExpression(final Expression operand) {
        this.myOperand = operand;
    }

    public Expression getOperand() {
        return myOperand;
    }

    public OperationType getType() {
        return myType;
    }

    public void setType(final OperationType type) {
        this.myType = type;
    }

    public String getOperation() {
        switch (myType) {
            case NOT:
                return "!";
            case NEGATE:
                return "-";
            case DOUBLE_CAST:
                return "(double) ";
            case INT_CAST:
                return "(int) ";
            case LONG_CAST:
                return "(long) ";
            case SHORT_CAST:
                return "(short) ";
            case BYTE_CAST:
                return "(byte) ";
            case FLOAT_CAST:
                return "(float) ";
            case CHAR_CAST:
                return "(char) ";
            case CHECK_CAST:
                return "("+myParam+") ";
            default:
                return "";
        }
    }

    public String getOperation(AbstractOperationPrinter operationPrinter) {
        switch (myType) {
            case NOT:
                return operationPrinter.getNotView();
            case NEGATE:
                return operationPrinter.getNegateView();
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
}
