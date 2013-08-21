package com.sdc.ast.expressions.identifiers;

import com.sdc.ast.OperationType;
import com.sdc.ast.expressions.Constant;
import com.sdc.ast.expressions.Expression;
import com.sdc.abstractLanguage.AbstractFrame;

public class Variable extends Identifier {
    private final int myIndex;
    private final AbstractFrame myAbstractFrame;
    private Expression name;

    public Variable(final int index, final AbstractFrame abstractFrame) {
        this.myIndex = index;
        this.myAbstractFrame = abstractFrame;
        myType = OperationType.VARIABLE;

    }

    @Override
    public Expression getName() {
        if(name == null) name = new Constant(myAbstractFrame.getLocalVariableName(myIndex), false);
        return name;
    }

    @Override
    public String getType() {
        return myAbstractFrame.getLocalVariableType(myIndex);
    }

    public int getIndex() {
        return myIndex;
    }

    public AbstractFrame getAbstractFrame() {
        return myAbstractFrame;
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
                ", name=" + (name != null ? name : " no name yet") + "}";
    }

    public boolean isThis(){
        return getName() instanceof Constant && ((Constant) getName()).isThis();
    }

    @Override
    public boolean isBoolean() {
        return getType().contains("boolean");
    }
}
