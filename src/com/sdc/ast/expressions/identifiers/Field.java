package com.sdc.ast.expressions.identifiers;

import com.sdc.ast.OperationType;
import com.sdc.ast.expressions.Constant;
import com.sdc.ast.expressions.Expression;

public class Field extends Identifier {
    private final String myName;
    private final String myIdentifierType;
    private String myStaticOwnerName;
    private Expression myOwner;

    public Field(final String name, final String type) {
        this.myName = name;
        this.myIdentifierType = type;
        myType = OperationType.FIELD;
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
    public Expression getName() {
        return new Constant(myName, false);
    }

    @Override
    public String getType() {
        return myIdentifierType;
    }

    @Override
    public boolean isBoolean() {
        return myIdentifierType.contains("boolean");
    }
}
