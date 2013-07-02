package com.sdc.ast.expressions.identifiers;

public class Field extends Identifier {
    private final String myName;

    public Field(final String name) {
        this.myName = name;
    }

    public String getName() {
        return myName;
    }
}
