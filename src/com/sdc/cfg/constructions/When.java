package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class When extends Construction {
    private final Expression myCondition;
    private Map<Expression, List<Construction>> myCases = new HashMap<Expression, List<Construction>>();
    private List<Construction> myDefaultCase = new ArrayList<Construction>();

    public When(final Expression condition) {
        this.myCondition = condition;
    }

    public Expression getCondition() {
        return myCondition;
    }

    public Map<Expression, List<Construction>> getCases() {
        return myCases;
    }

    public List<Construction> getDefaultCase() {
        return myDefaultCase;
    }

    public void setDefaultCase(final List<Construction> defaultCase) {
        this.myDefaultCase = defaultCase;
    }

    public void setCases(final Map<Expression, List<Construction>> cases) {
        this.myCases = cases;
    }

    public void addCase(final Expression key, final List<Construction> body) {
        myCases.put(key, body);
    }

    public boolean hasDefaultCase() {
        return myDefaultCase.isEmpty();
    }
}
