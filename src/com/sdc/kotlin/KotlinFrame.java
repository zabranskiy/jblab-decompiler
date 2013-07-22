package com.sdc.kotlin;

import com.sdc.abstractLanguage.AbstractFrame;

public class KotlinFrame extends AbstractFrame {
    private int myLastLocalVariableIndex = -1;

    public void setLastLocalVariableIndex(int lastLocalVariableIndex) {
        this.myLastLocalVariableIndex = lastLocalVariableIndex;
    }

    public int getLastLocalVariableIndex() {
        if (myLastLocalVariableIndex == -1) {
            myLastLocalVariableIndex = ((KotlinFrame) myParent).getLastLocalVariableIndex();
        }
        return myLastLocalVariableIndex;
    }

    @Override
    protected String getVariableNameForDeclaration(int index) {
        if (index <= myLastLocalVariableIndex) {
            return getLocalVariableName(index) + " : " + getLocalVariableType(index);
        } else {
            return "var " + getLocalVariableName(index) + " : " + getLocalVariableType(index);
        }
    }
}
