package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;

import org.jetbrains.annotations.NotNull;


public class DoWhile extends Loop {
    public DoWhile(final @NotNull Expression condition) {
        super(condition);
    }
}
