package com.sdc.ast.controlflow;

import com.sdc.ast.expressions.Expression;

import java.util.ArrayList;
import java.util.List;

public class Invocation extends Statement {
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
