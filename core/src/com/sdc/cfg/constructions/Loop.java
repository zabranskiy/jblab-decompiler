package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class Loop extends Construction {
    protected final Expression myCondition;
    protected Construction myBody;

    public Loop(final @NotNull Expression condition) {
        this.myCondition = condition;
    }

    @NotNull
    public Expression getCondition() {
        return myCondition;
    }

    @Nullable
    public Construction getBody() {
        return myBody;
    }

    public void setBody(final @NotNull Construction body) {
        this.myBody = body;
    }
}
