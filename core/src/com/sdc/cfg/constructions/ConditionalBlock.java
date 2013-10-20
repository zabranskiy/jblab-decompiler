package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ConditionalBlock extends Construction {
    private final Expression myCondition;
    private Construction myThenBlock;
    private Construction myElseBlock;

    public ConditionalBlock(final @NotNull Expression condition) {
        this.myCondition = condition;
    }

    @NotNull
    public Expression getCondition() {
        return myCondition;
    }

    @Nullable
    public Construction getThenBlock() {
        return myThenBlock;
    }

    public void setThenBlock(final @NotNull Construction thenBlock) {
        this.myThenBlock = thenBlock;
    }

    @Nullable
    public Construction getElseBlock() {
        return myElseBlock;
    }

    public void setElseBlock(final @NotNull Construction elseBlock) {
        this.myElseBlock = elseBlock;
    }

    public boolean hasElseBlock() {
        return myElseBlock != null;
    }
}
