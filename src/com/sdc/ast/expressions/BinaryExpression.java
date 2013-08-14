package com.sdc.ast.expressions;

import com.sdc.abstractLanguage.AbstractOperationPrinter;
import com.sdc.ast.OperationType;

import static com.sdc.ast.OperationType.*;

public class BinaryExpression extends PriorityExpression {
    private final Expression myLeft;
    private final Expression myRight;

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

    @Override
    public String toString() {
        return "BinaryExpression{" +
                "myLeft=" + myLeft +
                ", myRight=" + myRight +
                ", myType=" + myType +
                '}';
    }
    public boolean isArithmeticType(){
        return myType== ADD || myType == SUB || myType == MUL || myType == DIV || myType == REM;
    }
}
