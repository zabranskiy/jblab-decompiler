package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.identifiers.Variable;

public class ForEach extends Construction {
    private final Variable myVariable;
    private final Expression myContainer;
    private Construction myBody;

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

    public Construction getBody() {
        return myBody;
    }

    public void setBody(final Construction body) {
        this.myBody = body;
    }
}