package com.sdc.languages.kotlin.astUtils;

import com.sdc.languages.general.astUtils.Frame;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;

import org.jetbrains.annotations.NotNull;


public class KotlinFrame extends Frame {
    @NotNull
    @Override
    protected Frame createFrame() {
        return new KotlinFrame();
    }

    @NotNull
    @Override
    protected Variable createVariable(final int index, final @NotNull Type type, final @NotNull String name) {
        return new KotlinVariable(index, type, name);
    }
}
