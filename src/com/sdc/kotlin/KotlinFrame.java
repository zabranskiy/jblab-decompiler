package com.sdc.kotlin;

import com.sdc.abstractLanguage.AbstractFrame;

public class KotlinFrame extends AbstractFrame {
    @Override
    protected String getVariableNameForDeclaration(int index) {
        return getLocalVariableName(index) + " : " + getLocalVariableType(index);
    }
}
