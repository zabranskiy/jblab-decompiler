package com.sdc.kotlin;

import com.sdc.abstractLanguage.OldAbstractFrame;
import com.sdc.util.DeclarationWorker;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class OldKotlinFrame extends OldAbstractFrame {
    private List<Integer> myNotNullVariables = new ArrayList<Integer>();

    @Override
    protected String getVariableNameForDeclaration(final int index) {
        final String variableType = getLocalVariableType(index);

        final boolean notNeedNullableMark = isNotNullVariable(index) || DeclarationWorker.isPrimitiveClass(variableType) || variableType.endsWith("?");
        final String nullableMark = notNeedNullableMark ? "" : "?";

        String variableName = getLocalVariableName(index) + " : " + variableType;
        if (index > myLastLocalVariableIndex) {
            variableName = "var " + variableName;
        }
        return variableName + nullableMark;
    }

    public void addNotNullVariable(final int index) {
        myNotNullVariables.add(index);
    }

    public boolean isNotNullVariable(final int index) {
        final OldKotlinFrame sameFrame = (OldKotlinFrame) mySameAbstractFrame;
        final OldKotlinFrame parentFrame = (OldKotlinFrame) myParent;

        return myNotNullVariables.contains(index)
                || (sameFrame != null && sameFrame.isNotNullVariable(index))
                || (parentFrame != null && parentFrame.isNotNullVariable(index));
    }
}
