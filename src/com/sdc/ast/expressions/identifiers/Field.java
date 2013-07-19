package com.sdc.ast.expressions.identifiers;

public class Field extends Identifier {
    private final String myName;
    private final String myType;

    public Field(final String name, final String type) {
        this.myName = name;
        this.myType = type;
    }

    @Override
    public String getName() {
        return myName;
    }

    @Override
    public String getType() {
        return myType;
    }
}
