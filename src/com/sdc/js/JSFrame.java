package com.sdc.js;

import com.sdc.abstractLanguage.AbstractFrame;

public class JSFrame extends AbstractFrame {
    @Override
    protected String getVariableNameForDeclaration(final int index) {
        return myLocalVariableNames.get(index);
    }
}
