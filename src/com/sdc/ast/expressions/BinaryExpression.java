package com.sdc.ast.expressions;

import com.sdc.abstractLanguage.AbstractOperationPrinter;
import com.sdc.ast.OperationType;

import static com.sdc.ast.OperationType.*;

public class BinaryExpression extends PriorityExpression {
    protected Expression myLeft;
    protected Expression myRight;

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
            case BITWISE_AND:
                return operationPrinter.getBitwiseAndView();
            case BITWISE_OR:
                return operationPrinter.getBitwiseOrView();
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
            case BITWISE_XOR:
                return operationPrinter.getXorView();
            default:
                return "";
        }
    }

    public Expression invert() {
        switch (myType) {
            case AND:
                myType = OR;
                invertOperands();
                break;
            case OR:
                myType = AND;
                invertOperands();
                break;
            case EQ:
                myType = NE;
                break;
            case NE:
                myType = EQ;
                break;
            case GE:
                myType = LT;
                break;
            case LT:
                myType = GE;
                break;
            case LE:
                myType = GT;
                break;
            case GT:
                myType = LE;
                break;
            default:
                return super.invert();
        }
        return this;
    }

    private void invertOperands() {
        myLeft = myLeft.invert();
        myRight = myRight.invert();
    }

    @Override
    public String toString() {
        return "BinaryExpression{" +
                "myType=" + myType +
                ", myLeft=" + myLeft +
                ", myRight=" + myRight +
                '}';
    }

    public boolean isArithmeticType() {
        return myType == ADD || myType == SUB || myType == MUL || myType == DIV || myType == REM;
    }

    public boolean isLogicType() {
        return myType == EQ || myType == NE || myType == GE || myType == LE || myType == LT || myType == GT || myType == AND || myType == OR;
    }
}
