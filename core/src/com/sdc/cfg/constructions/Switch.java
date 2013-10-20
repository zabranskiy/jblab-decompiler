package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class Switch extends Construction {
    private final Expression myCondition;
    private List<SwitchCase> myCases = new ArrayList<SwitchCase>();

    public Switch(final @NotNull Expression condition) {
        this.myCondition = condition;
    }

    @NotNull
    public Expression getCondition() {
        return myCondition;
    }

    @NotNull
    public List<SwitchCase> getCases() {
        return myCases;
    }

    public void setCases(final @NotNull List<SwitchCase> cases) {
        this.myCases = cases;
    }

    public void addCase(final @NotNull SwitchCase switchCase) {
        myCases.add(switchCase);
    }
}