package com.sdc.ast.expressions.identifiers;

import com.sdc.ast.OperationType;
import com.sdc.ast.expressions.Constant;
import com.sdc.ast.expressions.Expression;

public class Variable extends Identifier {
    private final int myIndex;

    private Expression myName;
    private String myVariableType;

    private boolean myIsMethodParameter = false;
    private boolean myIsDeclared = false;

    public Variable(final int index, final String variableType, final String name) {
        this.myIndex = index;
        this.myName = new Constant(name, false);
        this.myVariableType = variableType;

        myType = OperationType.VARIABLE;
    }

    public void setIsMethodParameter(final boolean isMethodParameter) {
        this.myIsMethodParameter = isMethodParameter;
    }

    public boolean isMethodParameter() {
        return myIsMethodParameter;
    }

    public boolean isDeclared() {
        return myIsDeclared;
    }

    public void declare() {
        myIsDeclared = true;
    }

    @Override
    public Expression getName() {
        return myName;
    }

    @Override
    public String getType() {
        return myVariableType;
    }

    public int getIndex() {
        return myIndex;
    }

    @Override
    public String toString() {
        Expression name;
        try {
            name = getName();
        } catch (NullPointerException e) {
            name = null;
        }
        return "Variable{" +
                "myIndex=" + myIndex +
                ", myName=" + (name != null ? name : " no myName yet") + "}";
    }

    public boolean isThis() {
        return getName() instanceof Constant && ((Constant) getName()).isThis();
    }
}
