package com.sdc.java;

import com.sdc.abstractLanguage.AbstractFrame;

public class JavaFrame extends AbstractFrame {
    @Override
    protected String getVariableNameForDeclaration(int index) {
        return myLocalVariableTypes.get(index) + myLocalVariableNames.get(index);
    }
}
