package com.sdc.js;

import com.sdc.abstractLanguage.AbstractFrame;

public class Frame extends AbstractFrame {
    @Override
    protected String getVariableNameForDeclaration(final int index) {
        return myLocalVariableNames.get(index);
    }
}
