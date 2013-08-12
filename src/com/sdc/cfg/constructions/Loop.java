package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;

public class Loop extends Construction {
    protected final Expression myCondition;
    protected Construction myBody;

    public Loop(final Expression condition) {
        this.myCondition = condition;
    }

    public Expression getCondition() {
        return myCondition;
    }

    public Construction getBody() {
        return myBody;
    }

    public void setBody(final Construction body) {
        this.myBody = body;
    }
}
