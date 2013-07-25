package com.sdc.ast.controlflow;

import com.sdc.ast.expressions.Expression;

import java.util.ArrayList;
import java.util.List;

public class Invocation extends Statement {
    private final String myFunction;
    private final List<Expression> myArguments;
    private final String myReturnType;

    public Invocation(final String function, final String returnType, final List<Expression> arguments) {
        this.myFunction = function;
        this.myArguments = arguments;
        this.myReturnType = returnType;
    }

    public String getFunction() {
        return myFunction;
    }

    public String getReturnType() {
        return myReturnType;
    }

    public List<Expression> getArguments() {
        return myArguments;
    }
}
