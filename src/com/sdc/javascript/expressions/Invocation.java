package com.sdc.javascript.expressions;

import java.util.List;

public class Invocation extends Expression {
    public final String function;
    public final List<Expression> arguments;

    public Invocation (final String function, final List<Expression> arguments) {
        this.function = function;
        this.arguments = arguments;
    }
}
