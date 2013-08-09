package com.sdc.ast.expressions.identifiers;

public class Field extends Identifier {
    private final String myName;
    private final String myType;
    private String myStaticOwnerName;
    private Identifier myOwner;

    public Field(final String name, final String type) {
        this.myName = name;
        this.myType = type;
    }

    public void setStaticOwnerName(final String staticOwnerName) {
        this.myStaticOwnerName = staticOwnerName;
    }

    public void setOwner(final Identifier owner) {
        this.myOwner = owner;
    }

    public String getStaticOwnerName() {
        return myStaticOwnerName;
    }

    public Identifier getOwner() {
        return myOwner;
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
