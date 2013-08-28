package com.sdc.ast.expressions;

import com.sdc.abstractLanguage.AbstractOperationPrinter;
import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;

import static com.sdc.ast.ExpressionType.*;

public class BinaryExpression extends PriorityExpression {
    protected final Expression myLeft;
    protected final Expression myRight;

    public BinaryExpression(final ExpressionType expressionType, final Expression left, final Expression right) {
        super(expressionType, getMyType(expressionType,left,right));
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
        switch (myExpressionType) {
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
                return operationPrinter.getBitwiseXorView();
            default:
                return "";
        }
    }

    public Expression invert() {
        switch (myExpressionType) {
            case AND:
                return new BinaryExpression(OR,myLeft.invert(),myRight.invert());
            case OR:
                return new BinaryExpression(AND, myLeft.invert(),myRight.invert());
            case EQ:
                return new BinaryExpression(NE, myLeft,myRight);
            case NE:
                return new BinaryExpression(EQ, myLeft,myRight);
            case GE:
                return new BinaryExpression(LT, myLeft,myRight);
            case LT:
                return new BinaryExpression(GE, myLeft,myRight);
            case LE:
                return new BinaryExpression(GT, myLeft,myRight);
            case GT:
                return new BinaryExpression(LE, myLeft,myRight);
            default:
                return super.invert();
        }
    }

    @Override
    public String toString() {
        return "BinaryExpression{" +
                "myExpressionType=" + myExpressionType +
                ", myLeft=" + myLeft +
                ", myRight=" + myRight +
                '}';
    }

    public boolean isIncrementCastableType() {
        return myExpressionType == ADD || myExpressionType == SUB || myExpressionType == MUL || myExpressionType == DIV || myExpressionType == REM ||
                myExpressionType == USHR || myExpressionType == SHR || myExpressionType == SHL ||
                myExpressionType == BITWISE_AND || myExpressionType == BITWISE_OR || myExpressionType == BITWISE_XOR;
    }

    public boolean isLogicType() {
        return BinaryExpression.isLogicType(myExpressionType);
    }
    public static boolean isLogicType(ExpressionType expressionType) {
        return expressionType == EQ || expressionType == NE ||
                expressionType == GE || expressionType == LE || expressionType == LT || expressionType == GT ||
                expressionType == AND || expressionType == OR;
    }

    public static Type getMyType(final ExpressionType expressionType, final Expression left, final Expression right) {
        return isLogicType(expressionType) ? Type.BOOLEAN_TYPE : Type.getStrongerType(left.getType(), right.getType());
    }
    @Override
    public boolean findVariable(Variable variable) {
        return myLeft.findVariable(variable) || myRight.findVariable(variable);
    }
}
