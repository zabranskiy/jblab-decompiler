package com.sdc.ast.expressions;

import java.util.List;

public class Invocation extends Expression {
    private final String myFunction;
    private final List<Expression> myArguments;

    public Invocation(final String function, final List<Expression> arguments) {
        this.myFunction = function;
        this.myArguments = arguments;
    }

    public String getFunction() {
        return myFunction;
    }

    public List<Expression> getArguments() {
        return myArguments;
    }

}
