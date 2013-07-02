package com.sdc.ast.expressions.identifiers;

import com.sdc.java.Frame;

public class Variable extends Identifier {
    private final int myIndex;
    private final Frame myFrame;

    public Variable(final int index, final Frame frame) {
        this.myIndex = index;
        this.myFrame = frame;
    }

    public String getName() {
        return myFrame.getLocalVariableName(myIndex);
    }
}
