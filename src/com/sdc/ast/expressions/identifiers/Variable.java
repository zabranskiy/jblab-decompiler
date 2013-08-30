package com.sdc.ast.expressions.identifiers;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.Constant;
import com.sdc.ast.expressions.Expression;

public class Variable extends Identifier {
    protected final int myIndex;

    protected Constant myName;

    protected boolean myIsMethodParameter = false;
    protected boolean myIsDeclared = false;

    protected Variable myParentCopy;
    protected Variable myChildCopy;

    public Variable(final int index, final Type type, final String name) {
        super(ExpressionType.VARIABLE, type);
        this.myIndex = index;
        this.myName = new Constant(name, false, type);
    }

    public void setIsMethodParameter(final boolean isMethodParameter) {
        this.myIsMethodParameter = isMethodParameter;
    }

    public boolean isMethodParameter() {
        return myIsMethodParameter;
    }

    public boolean isDeclared() {
        return myIsDeclared;
    }

    public void declare() {
        myIsDeclared = true;
        if (myChildCopy != null) {
            myChildCopy.declare();
        }
    }

    public void cutParent() {
        if (myParentCopy != null) {
            myParentCopy.cutChildCopy();
        }

        myParentCopy = null;
    }

    public void setName(final Constant name) {
        this.myName = name;
    }

    public Variable createCopy() {
        Variable copy = createVariable(myIndex, getType(), ((Constant) myName).getValue().toString());
        myChildCopy = copy;
        copy.setParentCopy(this);
        if (isDeclared()) {
            copy.declare();
        }

        return copy;
    }

    @Override
    public Constant getName() {
        return myName;
    }

    public int getIndex() {
        return myIndex;
    }

    @Override
    public String toString() {
        Expression name;
        try {
            name = getName();
        } catch (NullPointerException e) {
            name = null;
        }
        return "Variable{" +
                "myIndex=" + myIndex +
                ", myName=" + (name != null ? name : " no myName yet") + "}";
    }

    public String nameToString(){
        return myName==null?"null":myName.valueToString();
    }
    public boolean isThis() {
        return getName().isThis();
    }

    protected void setParentCopy(final Variable parent) {
        this.myParentCopy = parent;
    }

    protected void cutChildCopy() {
        myChildCopy = null;
    }

    protected Variable createVariable(final int index, final Type type, final String name) {
        return new Variable(index, type, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return equalsWithChildren(o) || equalsWithParents(o);
    }

    public boolean equalsWithParents(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (myParentCopy == null) return false;
        else return myParentCopy.equalsWithParents(o);
    }

    public boolean equalsWithChildren(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (myChildCopy == null) return false;
        else return myChildCopy.equalsWithChildren(o);
    }

    @Override
    public int hashCode() {
        return myIndex;   //todo correct
    }
    public boolean isUndefined(){
        return myName.isNull();
    }

    @Override
    public boolean findVariable(Variable variable) {
        return equals(variable);
    }
}
