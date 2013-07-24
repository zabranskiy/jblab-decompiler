package com.sdc.kotlin;

import com.sdc.abstractLanguage.AbstractFrame;
import com.sdc.util.DeclarationWorker;

import java.util.ArrayList;
import java.util.List;

public class KotlinFrame extends AbstractFrame {
    private int myLastLocalVariableIndex = -1;
    private List<Integer> myNotNullVariables = new ArrayList<Integer>();

    public void setLastLocalVariableIndex(final int lastLocalVariableIndex) {
        this.myLastLocalVariableIndex = lastLocalVariableIndex;
    }

    public int getLastLocalVariableIndex() {
        if (myLastLocalVariableIndex == -1) {
            myLastLocalVariableIndex = ((KotlinFrame) myParent).getLastLocalVariableIndex();
        }
        return myLastLocalVariableIndex;
    }

    @Override
    protected String getVariableNameForDeclaration(final int index) {
        final String variableType = getLocalVariableType(index);
        final boolean notNeedNullableMark = isNotNullVariable(index) || DeclarationWorker.isPrimitiveClass(variableType) || variableType.endsWith("?");
        final String nullableMark = notNeedNullableMark ? "" : "?";

        String variableName;
        if (index <= myLastLocalVariableIndex) {
            variableName = getLocalVariableName(index) + " : " + getLocalVariableType(index);
        } else {
            variableName = "var " + getLocalVariableName(index) + " : " + getLocalVariableType(index);
        }
        return variableName + nullableMark;
    }

    public void addNotNullVariable(final int index) {
        myNotNullVariables.add(index);
    }

    public boolean isNotNullVariable(final int index) {
        final KotlinFrame sameFrame = (KotlinFrame) mySameAbstractFrame;
        final KotlinFrame parentFrame = (KotlinFrame) myParent;

        return myNotNullVariables.contains(index)
                || (sameFrame != null && sameFrame.isNotNullVariable(index))
                || (parentFrame != null && parentFrame.isNotNullVariable(index));
    }
}
