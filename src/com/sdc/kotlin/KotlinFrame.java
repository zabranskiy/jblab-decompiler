package com.sdc.kotlin;

import com.sdc.abstractLanguage.AbstractFrame;
import com.sdc.ast.expressions.identifiers.Variable;

public class KotlinFrame extends AbstractFrame {
    @Override
    protected AbstractFrame createFrame() {
        return new KotlinFrame();
    }

    @Override
    protected Variable createVariable(int index, String type, String name) {
        return new KotlinVariable(index, type, name);
    }
}
