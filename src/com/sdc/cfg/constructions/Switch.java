package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Switch extends Construction {
    private final Expression myCondition;
    private Map<Integer, List<Construction>> myCases = new HashMap<Integer, List<Construction>>();
    private List<Construction> myDefaultCase = new ArrayList<Construction>();

    public Switch(final Expression condition) {
        this.myCondition = condition;
    }

    public Expression getCondition() {
        return myCondition;
    }

    public Map<Integer, List<Construction>> getCases() {
        return myCases;
    }

    public List<Construction> getDefaultCase() {
        return myDefaultCase;
    }

    public void setDefaultCase(final List<Construction> defaultCase) {
        this.myDefaultCase = defaultCase;
    }

    public void setCases(final Map<Integer, List<Construction>> cases) {
        this.myCases = cases;
    }

    public void addCase(final Integer key, final List<Construction> body) {
        myCases.put(key, body);
    }

    public boolean hasDefaultCase() {
        return myDefaultCase.isEmpty();
    }
}
