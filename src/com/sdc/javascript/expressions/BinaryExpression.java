package com.sdc.javascript.expressions;

public class BinaryExpression extends Expression {
    public enum OperationType {
        ADD, SUB, MUL, DIV, MOD, LSHIFT, SRSHIFT, URSHIFT,
        LESS, LE, GREATER, GE, EQUALS, NEQUALS, SEQUALS, SNEQUALS,
        AND, OR, BITAND, BITXOR, BITOR
    }

    public final Expression left;
    public final Expression right;
    public final OperationType type;

    public BinaryExpression(final OperationType type, final Expression left, final Expression right) {
        this.type = type;
        this.left = left;
        this.right = right;
    }
}
