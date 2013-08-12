package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;

import java.util.ArrayList;
import java.util.List;

public class Loop extends Construction {
    protected final Expression myCondition;
    protected List<Construction> myBody = new ArrayList<Construction>();

    public Loop(final Expression condition) {
        this.myCondition = condition;
    }

    public Expression getCondition() {
        return myCondition;
    }

    public List<Construction> getBody() {
        return myBody;
    }

    public void setBody(final List<Construction> body) {
        this.myBody = body;
    }

    public void addConstructionToBody(final Construction construction) {
        myBody.add(construction);
    }
}
