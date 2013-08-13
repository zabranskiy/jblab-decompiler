package com.sdc.ast.expressions;

import com.sdc.ast.OperationType;

import static com.sdc.ast.OperationType.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitrii.Pozdin
 * Date: 8/12/13
 * Time: 10:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExprIncrement extends Expression {
    private Expression myOperand;
    private int myIncrement;
    private OperationType myType;



    public ExprIncrement(final Expression operand, final int increment, final OperationType type) {
        myOperand = operand;
        myIncrement = increment;
        myType = type;
        switch (type) {
            case INC:
                myIncrement = 1; //i.e. we ignore increment here
                myType = INC;
                return;
            case DEC:
                myIncrement = 1; //i.e. we ignore increment here
                myType = DEC;
                return;
            case ADD_INC:
                addType(increment);
                return;
            case ADD:
                addType(increment);
                return;
            case SUB_INC:
                subType(increment);
                return;
            case SUB:
                subType(increment);
                return;
            case MUL_INC:
                myType = MUL_INC;
                return;
            case MUL:
                myType = MUL_INC;
                return;
            case DIV_INC:
                myType = DIV_INC;
                return;
            case DIV:
                myType = DIV_INC;
                return;
            case REM_INC:
                myType = REM_INC;
                return;
            case REM:
                myType = REM_INC;
                return;
            default:
                return;
        }
    }

    public ExprIncrement(final Expression operand, final int increment) {
        myOperand = operand;
        myIncrement = Math.abs(increment);
        if (increment == 1) {
            myType = INC;
        } else if (increment == -1) {
            myType = DEC;
        } else if (increment >=0) {
            myType = ADD_INC;
        } else if (increment <0) {
            myType = SUB_INC;
        }
    }


    public String getOperation() {
        switch (myType) {
            case INC:
                return "++";
            case DEC:
                return "--";
            case ADD_INC:
                return " += " + myIncrement;
            case SUB_INC:
                return " -= " + myIncrement;
            case MUL_INC:
                return " *= " + myIncrement;
            case DIV_INC:
                return " /= " + myIncrement;
            case REM_INC:
                return " %= " + myIncrement;
            default:
                return "";
        }
    }

    public Expression getOperand() {
        return myOperand;
    }

    public int getIncrement() {
        return myIncrement;
    }

    public OperationType getType() {
        return myType;
    }

    private void addType(int increment) {
        if (increment == 1) {
            myType = INC;
        } else if (increment == -1) {
            myType = DEC;
        } else if (increment > 0) {
            myType = ADD_INC;
        } else if (increment < 0) {
            myType = SUB_INC;
        }
        myIncrement = Math.abs(myIncrement);
    }

    private void subType(int increment) {
        if (increment == 1) {
            myType = DEC;
        } else if (increment == -1) {
            myType = INC;
        } else if (increment > 0) {
            myType = ADD_INC;
        } else if (increment < 0) {
            myType = SUB_INC;
        }
        myIncrement = Math.abs(myIncrement);
    }
}
