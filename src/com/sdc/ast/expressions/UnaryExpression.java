package com.sdc.ast.expressions;

public class UnaryExpression extends Expression {
    public static enum OperationType {
        NOT, NEGATE, DOUBLE_CAST, INT_CAST, LONG_CAST, SHORT_CAST, BYTE_CAST, CHAR_CAST, FLOAT_CAST, CHECK_CAST
    }

    private final Expression myOperand;
    private OperationType myType;
    private String myParam="";     // for CHECK_CAST

    public UnaryExpression(final OperationType type, final Expression operand) {
        this.myType = type;
        this.myOperand = operand;
        if (myType == OperationType.NOT || myType == OperationType.NEGATE) {
            setDoubleLength(operand.hasDoubleLength());
        } else {
            setDoubleLength(myType == OperationType.DOUBLE_CAST || myType == OperationType.LONG_CAST);
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
}
