package com.sdc.js;

import com.sdc.abstractLanguage.OldAbstractFrame;

@Deprecated
public class OldJSFrame extends OldAbstractFrame {
    @Override
    protected String getVariableNameForDeclaration(final int index) {
        return myLocalVariableNames.get(index);
    }
}
