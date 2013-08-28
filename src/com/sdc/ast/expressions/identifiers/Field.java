package com.sdc.ast.expressions.identifiers;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.Constant;
import com.sdc.ast.expressions.Expression;

public class Field extends Identifier {
    private final String myName;
    private String myStaticOwnerName;
    private Expression myOwner;

    public Field(final String name, final Type type) {
        super(ExpressionType.FIELD, type);
        this.myName = name;
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
        return new Constant(myName, false, getType());
    }

    @Override
    public boolean findVariable(Variable variable) {
        return false;
    }
}
