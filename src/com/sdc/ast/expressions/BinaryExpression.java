package com.sdc.ast.expressions;

import com.sdc.abstractLanguage.AbstractOperationPrinter;

public class BinaryExpression extends Expression {
    public enum OperationType {
        ADD, SUB, MUL, DIV, REM,
        AND, OR, EQ, NE, GE, GT, LE, LT, SHR, SHL,USHR, XOR
    }

    private final Expression myLeft;
    private final Expression myRight;
    private OperationType myType;

    public BinaryExpression(final OperationType type, final Expression left, final Expression right) {
        this.myType = type;
        this.myLeft = left;
        this.myRight = right;
        setDoubleLength(left.hasDoubleLength() && right.hasDoubleLength());
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
            case EQ:
                return "== ";
            case NE:
                return "!= ";
            case GE:
                return ">= ";
            case GT:
                return "> ";
            case LE:
                return "<= ";
            case LT:
                return "< ";
            default:
                return "";
        }
    }

    public String getOperation(AbstractOperationPrinter operationPrinter) {
        switch (myType) {
            case ADD:
                return operationPrinter.getAddView();
            case SUB:
                return operationPrinter.getSubView();
            case DIV:
                return operationPrinter.getDivView();
            case MUL:
                return operationPrinter.getMulView();
            case REM:
                 return operationPrinter.getRemView();
            case AND:
                return operationPrinter.getBitewiseAndView();
            case OR:
                return operationPrinter.getBitewseOrView();
            case EQ:
                return operationPrinter.getEqualityView();
            case NE:
                return operationPrinter.getNotEqualityView();
            case GE:
                return operationPrinter.getGEView();
            case GT:
                return operationPrinter.getGreaterView();
            case LE:
                return operationPrinter.getLEView();
            case LT:
                return operationPrinter.getLessView();
            case SHL:
                return operationPrinter.getSHLView();
            case SHR:
                return operationPrinter.getSHRView();
            case USHR:
                return operationPrinter.getUSHRView();
            case XOR:
                return operationPrinter.getXorView();
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

    @Override
    public String toString() {
        return "BinaryExpression{" +
                "myLeft=" + myLeft +
                ", myRight=" + myRight +
                ", myType=" + myType +
                '}';
    }
}
