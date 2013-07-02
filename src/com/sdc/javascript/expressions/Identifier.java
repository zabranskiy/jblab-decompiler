package com.sdc.javascript.expressions;

public class Identifier extends Expression {
    private final String myName;

    public Identifier(final String name) {
        this.myName = name;
    }

    public String getName() {
        return myName;
    }
}
