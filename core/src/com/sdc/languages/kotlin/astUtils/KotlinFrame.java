package com.sdc.languages.kotlin.astUtils;

import com.sdc.languages.general.astUtils.Frame;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;

public class KotlinFrame extends Frame {
    @Override
    protected Frame createFrame() {
        return new KotlinFrame();
    }

    @Override
    protected Variable createVariable(int index, Type type, String name) {
        return new KotlinVariable(index, type, name);
    }
}
