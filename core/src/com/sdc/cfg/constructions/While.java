package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;

import org.jetbrains.annotations.NotNull;


public class While extends Loop {
    public While(final @NotNull Expression condition) {
        super(condition);
    }
}
