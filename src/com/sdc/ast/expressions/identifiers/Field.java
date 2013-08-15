package com.sdc.ast.expressions.identifiers;

import com.sdc.ast.expressions.Expression;

public class Field extends Identifier {
    private final String myName;
    private final String myType;
    private String myStaticOwnerName;
    private Expression myOwner;

    public Field(final String name, final String type) {
        this.myName = name;
        this.myType = type;
    }

    public void setStaticOwnerName(final String staticOwnerName) {
        this.myStaticOwnerName = staticOwnerName;
    }

    public void setOwner(final Expression owner) {
        this.myOwner = owner;
    }

    public String getStaticOwnerName() {
        return myStaticOwnerName;
    }

    public Expression getOwner() {
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
