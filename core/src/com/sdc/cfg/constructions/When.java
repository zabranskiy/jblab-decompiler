package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


public class When extends Construction {
    private Expression myCondition;
    private Map<Expression, Construction> myCases = new HashMap<Expression, Construction>();
    private Construction myDefaultCase;

    public When(final @NotNull Expression condition) {
        this.myCondition = condition;
    }

    @NotNull
    public Expression getCondition() {
        return myCondition;
    }

    @NotNull
    public Map<Expression, Construction> getCases() {
        return myCases;
    }

    @Nullable
    public Construction getDefaultCase() {
        return myDefaultCase;
    }

    public void setDefaultCase(final @NotNull Construction defaultCase) {
        this.myDefaultCase = defaultCase;
    }

    public void setCases(final @NotNull Map<Expression, Construction> cases) {
        this.myCases = cases;
    }

    public void addCase(final @NotNull Expression key, final @NotNull Construction body) {
        myCases.put(key, body);
    }

    public boolean hasEmptyDefaultCase() {
        return myDefaultCase == null;
    }

    public void setCondition(final @NotNull Expression condition) {
        this.myCondition = condition;
    }
}
