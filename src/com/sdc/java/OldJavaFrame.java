package com.sdc.java;

import com.sdc.abstractLanguage.OldAbstractFrame;

@Deprecated
public class OldJavaFrame extends OldAbstractFrame {
    @Override
    protected String getVariableNameForDeclaration(int index) {
        return myLocalVariableTypes.get(index) + myLocalVariableNames.get(index);
    }
}
