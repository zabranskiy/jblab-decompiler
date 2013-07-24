package com.sdc.ast.expressions.identifiers;

import com.sdc.abstractLangauge.AbstractFrame;

public class Variable extends Identifier {
    private final int myIndex;
    private final AbstractFrame myFrame;

    public Variable(final int index, final AbstractFrame frame) {
        this.myIndex = index;
        this.myFrame = frame;
    }

    public String getName() {
        return myFrame.getLocalVariableName(myIndex);
    }
}
