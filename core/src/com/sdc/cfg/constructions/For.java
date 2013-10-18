package com.sdc.cfg.constructions;

import com.sdc.ast.controlflow.Assignment;
import com.sdc.ast.controlflow.Statement;
import com.sdc.ast.expressions.Expression;

public class For extends Loop {
    private final Assignment myVariableInitialization;
    private final Statement myAfterThought;

    public For(final Assignment variableInitialization, final Expression condition, final Statement afterThought) {
        super(condition);
        this.myVariableInitialization = variableInitialization;
        this.myAfterThought = afterThought;
    }

    public Assignment getVariableInitialization() {
        return myVariableInitialization;
    }

    public Statement getAfterThought() {
        return myAfterThought;
    }
}
