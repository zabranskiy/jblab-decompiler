package com.sdc.ast.expressions;

import com.sdc.languages.general.printers.OperationPrinter;
import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.controlflow.Increment;
import com.sdc.ast.expressions.identifiers.Variable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.sdc.ast.ExpressionType.*;

public class ExprIncrement extends PriorityExpression {
    private final Variable myVariable;
    private final Expression myIncrement;
    private final boolean myIsIncrementSimple;

    public ExprIncrement(final @NotNull Increment increment) {
        this(increment.getVariable(), increment.getIncrementExpression(), increment.getOperationType());
    }

    public ExprIncrement(final @NotNull Variable variable,
                         final @NotNull Expression increment,
                         final @NotNull ExpressionType type) {
        super(geIncrementExpressionType(increment, type), variable.getType());
        this.myVariable = variable;
        this.myIncrement = increment;
        switch (myExpressionType) {
            case INC:
            case INC_REV:
            case DEC:
            case DEC_REV:
                this.myIsIncrementSimple = true;
                break;
            default:
                this.myIsIncrementSimple = false;
        }

    }

    public ExprIncrement(final @NotNull Variable variable, final int increment) {
        super(checkExpressionType(increment), variable.getType());
        myVariable = variable;
        myIncrement = new Constant(increment, false, Type.INT_TYPE);
        myIsIncrementSimple = increment == 1 || increment == -1;
    }

    @NotNull
    private static ExpressionType geIncrementExpressionType(final @NotNull Expression increment,
                                                            final @NotNull ExpressionType type) {
        switch (type) {
            case INC:
                return INC;
            case DEC:
                return DEC;
            case INC_REV:
                return INC_REV;
            case DEC_REV:
                return DEC_REV;
            case ADD:
                if (increment instanceof IntConstant && ((IntConstant) increment).isOne()) {
                    return INC;
                } else if (increment instanceof IntConstant && ((IntConstant) increment).isMinusOne()) {
                    return DEC;
                } else {
                    return ADD_INC;
                }
            case SUB:
                if (increment instanceof IntConstant && ((IntConstant) increment).isOne()) {
                    return DEC;
                } else if (increment instanceof IntConstant && ((IntConstant) increment).isMinusOne()) {
                    return INC;
                } else {
                    return SUB_INC;
                }
            case MUL:
                return MUL_INC;
            case DIV:
                return DIV_INC;
            case REM:
                return REM_INC;
            case SHL:
                return SHL_INC;
            case SHR:
                return SHR_INC;
            case USHR:
                return USHR_INC;
            case BITWISE_AND:
                return BITWISE_AND_INC;
            case BITWISE_OR:
                return BITWISE_OR_INC;
            case BITWISE_XOR:
                return BITWISE_XOR_INC;
            default:
                return AND; //for example
        }
    }

    @NotNull
    private static ExpressionType checkExpressionType(final int increment) {
        if (increment == 1) {
            return INC;
        } else if (increment == -1) {
            return DEC;
        } else if (increment >= 0) {
            return ADD_INC;
        } else {
            return SUB_INC;
        }
    }

    @NotNull
    public String getOperation(final @NotNull OperationPrinter operationPrinter) {
        switch (myExpressionType) {
            case INC:
                return operationPrinter.getIncView();
            case DEC:
                return operationPrinter.getDecView();
            case INC_REV:
                return operationPrinter.getIncRevView();
            case DEC_REV:
                return operationPrinter.getDecRevView();
            case ADD_INC:
                return operationPrinter.getAddIncView();
            case SUB_INC:
                return operationPrinter.getSubIncView();
            case MUL_INC:
                return operationPrinter.getMulIncView();
            case DIV_INC:
                return operationPrinter.getDivIncView();
            case REM_INC:
                return operationPrinter.getRemIncView();
            case USHR_INC:
                return operationPrinter.getUSHRIncView();
            case SHR_INC:
                return operationPrinter.getSHRIncView();
            case SHL_INC:
                return operationPrinter.getSHLIncView();
            case BITWISE_AND_INC:
                return operationPrinter.getBitwiseAndIncView();
            case BITWISE_OR_INC:
                return operationPrinter.getBitwiseOrIncView();
            case BITWISE_XOR_INC:
                return operationPrinter.getBitwiseXorIncView();
            default:
                return "";
        }
    }

    @NotNull
    public Variable getVariable() {
        return myVariable;
    }

    @NotNull
    public Expression getIncrementExpression() {
        return myIncrement;
    }

    public boolean isIncrementSimple() {
        return myIsIncrementSimple;
    }

    @NotNull
    @Override
    public String toString() {
        return "ExprIncrement{"
                + "myExpressionType=" + myExpressionType
                + ", myVariable=" + myVariable
                + ", myIncrement=" + myIncrement
                + '}';
    }

    @NotNull
    public Expression getVariableName() {
        return myVariable.getName();
    }

    @Override
    public boolean findVariable(final @NotNull Variable variable) {
        return myIncrement.findVariable(variable) || myVariable.equals(variable);
    }

    @NotNull
    @Override
    public List<Expression> getSubExpressions() {
        final List<Expression> subExpressions = new ArrayList<Expression>();
        subExpressions.add(myVariable);
        subExpressions.add(myIncrement);
        return subExpressions;
    }
}
