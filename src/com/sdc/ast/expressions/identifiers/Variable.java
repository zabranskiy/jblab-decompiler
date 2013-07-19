package com.sdc.ast.expressions.identifiers;

import com.sdc.ast.expressions.Expression;
import com.sdc.util.Frame;

public class Variable extends Identifier {
    private final int myIndex;
    private final Frame myFrame;

    private final Expression myArrayIndex;
    private final Identifier myArrayVariable;

    public Variable(final int index, final Frame frame) {
        this.myIndex = index;
        this.myFrame = frame;
        this.myArrayIndex = null;
        this.myArrayVariable = null;
    }

    public Variable(final Expression arrayIndex, final Identifier arrayVariable) {
        this.myIndex = -1;
        this.myFrame = null;
        this.myArrayIndex = arrayIndex;
        this.myArrayVariable = arrayVariable;
    }

    @Override
    public String getName() {
        if (myIndex != -1) {
            return myFrame.getLocalVariableName(myIndex);
        } else {
            return myArrayVariable.getName();
        }
    }

    @Override
    public String getType() {
        if (myIndex != -1) {
            return myFrame.getLocalVariableType(myIndex);
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
}
