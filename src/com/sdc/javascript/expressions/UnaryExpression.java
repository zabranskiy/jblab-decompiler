package com.sdc.javascript.expressions;

public class UnaryExpression extends Expression {
    public static enum OperationType {
        NOT, NEGATE, INC, DEC
    }

    public final OperationType type;
    public final Expression operand;

    public UnaryExpression(final OperationType type, final Expression operand) {
        this.type = type;
        this.operand = operand;
    }
}
