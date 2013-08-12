package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;

import java.util.ArrayList;
import java.util.List;

public class ConditionalBlock extends Construction {
    private final Expression myCondition;
    private List<Construction> myThenBlock = new ArrayList<Construction>();
    private List<Construction> myElseBlock = new ArrayList<Construction>();

    public ConditionalBlock(final Expression condition) {
        this.myCondition = condition;
    }

    public Expression getCondition() {
        return myCondition;
    }

    public List<Construction> getThenBlock() {
        return myThenBlock;
    }

    public void setThenBlock(final List<Construction> thenBlock) {
        this.myThenBlock = thenBlock;
    }

    public List<Construction> getElseBlock() {
        return myElseBlock;
    }

    public void setElseBlock(final List<Construction> elseBlock) {
        this.myElseBlock = elseBlock;
    }

    public boolean hasElseBlock() {
        return !myElseBlock.isEmpty();
    }

    public void addConstructionToThenBlock(final Construction construction) {
        myThenBlock.add(construction);
    }

    public void addConstructionToElseBlock(final Construction construction) {
        myElseBlock.add(construction);
    }
}
