package com.sdc.ast.expressions;

public class UnaryExpression extends Expression {
    public static enum OperationType {
        NOT, NEGATE
    }

    private final Expression myOperand;
    private OperationType myType;

    public UnaryExpression(final OperationType type, final Expression operand) {
        this.myType = type;
        this.myOperand = operand;
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
            default:
                return "";
        }
    }
}
