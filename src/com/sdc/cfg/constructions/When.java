package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;

import java.util.HashMap;
import java.util.Map;

public class When extends Construction {
    private final Expression myCondition;
    private Map<Expression, Construction> myCases = new HashMap<Expression, Construction>();
    private Construction myDefaultCase;

    public When(final Expression condition) {
        this.myCondition = condition;
    }

    public Expression getCondition() {
        return myCondition;
    }

    public Map<Expression, Construction> getCases() {
        return myCases;
    }

    public Construction getDefaultCase() {
        return myDefaultCase;
    }

    public void setDefaultCase(final Construction defaultCase) {
        this.myDefaultCase = defaultCase;
    }

    public void setCases(final Map<Expression, Construction> cases) {
        this.myCases = cases;
    }

    public void addCase(final Expression key, final Construction body) {
        myCases.put(key, body);
    }

    public boolean hasDefaultCase() {
        return myDefaultCase != null;
    }
}
