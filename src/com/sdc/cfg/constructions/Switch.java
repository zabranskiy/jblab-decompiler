package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;

import java.util.HashMap;
import java.util.Map;

public class Switch extends Construction {
    private final Expression myCondition;
    private Map<Integer, Construction> myCases = new HashMap<Integer, Construction>();
    private Construction myDefaultCase;

    public Switch(final Expression condition) {
        this.myCondition = condition;
    }

    public Expression getCondition() {
        return myCondition;
    }

    public Map<Integer, Construction> getCases() {
        return myCases;
    }

    public Construction getDefaultCase() {
        return myDefaultCase;
    }

    public void setDefaultCase(final Construction defaultCase) {
        this.myDefaultCase = defaultCase;
    }

    public void setCases(final Map<Integer, Construction> cases) {
        this.myCases = cases;
    }

    public void addCase(final Integer key, final Construction body) {
        myCases.put(key, body);
    }

    public boolean hasDefaultCase() {
        return myDefaultCase != null;
    }
}
