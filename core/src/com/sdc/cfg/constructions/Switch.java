package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;

import java.util.ArrayList;
import java.util.List;

public class Switch extends Construction {
    private final Expression myCondition;
    private List<SwitchCase> myCases = new ArrayList<SwitchCase>();

    public Switch(final Expression condition) {
        this.myCondition = condition;
    }

    public Expression getCondition() {
        return myCondition;
    }

    public List<SwitchCase> getCases() {
        return myCases;
    }

    public void setCases(final List<SwitchCase> cases) {
        this.myCases = cases;
    }

    public void addCase(final SwitchCase switchCase) {
        myCases.add(switchCase);
    }
}