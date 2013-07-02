package com.sdc.ast.expressions;

public class BinaryExpression extends Expression {
    public enum OperationType {
        ADD, SUB, MUL, DIV,
        AND, OR, EQUALITY, INEQUALITY, GE, GREATER, LE, LESS
    }

    private final Expression myLeft;
    private final Expression myRight;
    private OperationType myType;

    public BinaryExpression(final OperationType type, final Expression left, final Expression right) {
        this.myType = type;
        this.myLeft = left;
        this.myRight = right;
    }

    public BinaryExpression(final Expression left, final Expression right) {
        this.myLeft = left;
        this.myRight = right;
    }

    public Expression getLeft() {
        return myLeft;
    }

    public Expression getRight() {
        return myRight;
    }

    public void setType(final OperationType type) {
        this.myType = type;
    }

    public OperationType getOperationType() {
        return myType;
    }

    public String getOperation() {
        switch (myType) {
            case ADD:
                return "+ ";
            case SUB:
                return "- ";
            case DIV:
                return "/ ";
            case MUL:
                return "* ";
            case AND:
                return "&& ";
            case OR:
                return "|| ";
            case EQUALITY:
                return "== ";
            case INEQUALITY:
                return "!= ";
            case GE:
                return ">= ";
            case GREATER:
                return "> ";
            case LE:
                return "<= ";
            case LESS:
                return "< ";
            default:
                return "";
        }
    }

    public int getPriority() {
        switch (myType) {
            case ADD:
                return 0;
            case SUB:
                return 1;
            case MUL:
                return 3;
            case DIV:
                return 4;
            default:
                return -1;
        }
    }
}
