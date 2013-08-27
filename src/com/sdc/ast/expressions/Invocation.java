package com.sdc.ast.expressions;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;

import java.util.List;

public class Invocation extends PriorityExpression {
    private final String myFunction;
    private final List<Expression> myArguments;

    public Invocation(final String function, final Type type, final List<Expression> arguments) {
        super(ExpressionType.INVOCATION, type);
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
