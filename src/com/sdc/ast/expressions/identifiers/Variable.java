package com.sdc.ast.expressions.identifiers;

import com.sdc.ast.expressions.Expression;
import com.sdc.abstractLanguage.AbstractFrame;

public class Variable extends Identifier {
    private final int myIndex;
    private final AbstractFrame myAbstractFrame;

    private final Expression myArrayIndex;
    private final Identifier myArrayVariable;

    public Variable(final int index, final AbstractFrame abstractFrame) {
        this.myIndex = index;
        this.myAbstractFrame = abstractFrame;
        this.myArrayIndex = null;
        this.myArrayVariable = null;
    }

    public Variable(final Expression arrayIndex, final Identifier arrayVariable) {
        this.myIndex = -1;
        this.myAbstractFrame = null;
        this.myArrayIndex = arrayIndex;
        this.myArrayVariable = arrayVariable;
    }

    @Override
    public String getName() {
        if (myIndex != -1) {
            return myAbstractFrame.getLocalVariableName(myIndex);
        } else {
            return myArrayVariable.getName();
        }
    }

    @Override
    public String getType() {
        if (myIndex != -1) {
            return myAbstractFrame.getLocalVariableType(myIndex);
        } else {
            String result = myArrayVariable.getType();
            if (result.endsWith("[] ")) {
                result = result.substring(0, result.length() - 3);
            }

            return result + " ";
        }
    }

    public Expression getArrayIndex() {
        return myArrayIndex;
    }

    public Identifier getArrayVariable() {
        return myArrayVariable;
    }

    public int getIndex() {
        return myIndex;
    }

    public AbstractFrame getAbstractFrame() {
        return myAbstractFrame;
    }

    @Override
    public String toString() {
        String name;
        try {
            name = getName();
        } catch (NullPointerException e) {
            name = null;
        }
        return "Variable{" +
                "myIndex=" + myIndex +
                ", name=" + (name != null ? name : " no name yet") + "}";
    }
}
