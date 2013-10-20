package com.sdc.cfg.constructions;

import com.sdc.ast.controlflow.Assignment;
import com.sdc.ast.controlflow.Statement;
import com.sdc.ast.expressions.Expression;

import org.jetbrains.annotations.NotNull;


public class For extends Loop {
    private final Assignment myVariableInitialization;
    private final Statement myAfterThought;

    public For(final @NotNull Assignment variableInitialization,
               final @NotNull Expression condition,
               final @NotNull Statement afterThought) {
        super(condition);
        this.myVariableInitialization = variableInitialization;
        this.myAfterThought = afterThought;
    }

    @NotNull
    public Assignment getVariableInitialization() {
        return myVariableInitialization;
    }

    @NotNull
    public Statement getAfterThought() {
        return myAfterThought;
    }
}
