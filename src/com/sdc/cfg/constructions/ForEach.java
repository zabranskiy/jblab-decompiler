package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.identifiers.Variable;

import java.util.ArrayList;
import java.util.List;

public class ForEach extends Construction {
    private final Variable myVariable;
    private final Expression myContainer;
    private List<Construction> myBody = new ArrayList<Construction>();

    public ForEach(final Variable variable, final Expression container) {
        this.myVariable = variable;
        this.myContainer = container;
    }

    public Variable getVariable() {
        return myVariable;
    }

    public Expression getContainer() {
        return myContainer;
    }

    public List<Construction> getBody() {
        return myBody;
    }

    public void setBody(final List<Construction> body) {
        this.myBody = body;
    }

    public void addConstructionToBody(final Construction construction) {
        myBody.add(construction);
    }
}
