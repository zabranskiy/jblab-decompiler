package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;

public class ConditionalBlock extends Construction {
    private final Expression myCondition;
    private Construction myThenBlock;
    private Construction myElseBlock;

    public ConditionalBlock(final Expression condition) {
        this.myCondition = condition;
    }

    public Expression getCondition() {
        return myCondition;
    }

    public Construction getThenBlock() {
        return myThenBlock;
    }

    public void setThenBlock(final Construction thenBlock) {
        this.myThenBlock = thenBlock;
    }

    public Construction getElseBlock() {
        return myElseBlock;
    }

    public void setElseBlock(final Construction elseBlock) {
        this.myElseBlock = elseBlock;
    }

    public boolean hasElseBlock() {
        return myElseBlock != null;
    }
}
