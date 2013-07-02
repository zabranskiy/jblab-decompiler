package com.sdc.ast.controlflow;

import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.identifiers.Variable;

import java.util.List;

public class InstanceInvocation extends Invocation {
    private final Variable myVariable;

    public Variable getVariable() {
        return myVariable;
    }

    public InstanceInvocation(final String function, final List<Expression> arguments, final Variable variable) {
        super(function, arguments);
        this.myVariable = variable;
    }
}
