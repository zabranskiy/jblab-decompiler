package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.identifiers.Variable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class ForEach extends Construction {
    private List<Variable> myVariables;
    private final Expression myContainer;
    private Construction myBody;

    public ForEach(final @NotNull List<Variable> variables, final @NotNull Expression container) {
        this.myVariables = variables;
        this.myContainer = container;
    }

    @NotNull
    public List<Variable> getVariables() {
        return myVariables;
    }

    public void setVariables(final @NotNull List<Variable> variables) {
        this.myVariables = variables;
    }

    @NotNull
    public Expression getContainer() {
        return myContainer;
    }

    @Nullable
    public Construction getBody() {
        return myBody;
    }

    public void setBody(final @NotNull Construction body) {
        this.myBody = body;
    }
}