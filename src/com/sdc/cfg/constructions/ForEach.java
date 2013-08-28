package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.identifiers.Variable;

import java.util.List;

public class ForEach extends Construction {
    private List<Variable> myVariables;
    private final Expression myContainer;
    private Construction myBody;

    public ForEach(final List<Variable> variables, final Expression container) {
        this.myVariables = variables;
        this.myContainer = container;
    }

    public List<Variable> getVariables() {
        return myVariables;
    }

    public void setVariables(final List<Variable> variables) {
        this.myVariables = variables;
    }

    public Expression getContainer() {
        return myContainer;
    }

    public Construction getBody() {
        return myBody;
    }

    public void setBody(final Construction body) {
        this.myBody = body;
    }
}