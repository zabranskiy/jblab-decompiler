package com.sdc.ast.expressions.identifiers;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.Constant;

import org.jetbrains.annotations.NotNull;


public class Variable extends Identifier {
    protected final int myIndex;

    protected Constant myName;

    protected boolean myIsMethodParameter = false;
    protected boolean myIsDeclared = false;

    protected Variable myParentCopy;
    protected Variable myChildCopy;

    public Variable(final int index, final @NotNull Type type, final @NotNull String name) {
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

    public void setName(final @NotNull Constant name) {
        this.myName = name;
    }

    @NotNull
    public Variable createCopy() {
        final Variable copy = createVariable(myIndex, getType(), myName.getValue().toString());
        myChildCopy = copy;
        copy.setParentCopy(this);
        if (isDeclared()) {
            copy.declare();
        }

        return copy;
    }

    @NotNull
    @Override
    public Constant getName() {
        return myName;
    }

    public int getIndex() {
        return myIndex;
    }

    @NotNull
    @Override
    public String toString() {
        return "Variable{" + "myIndex=" + myIndex + ", myName=" + getName() + "}";
    }

    @NotNull
    public String nameToString() {
        return myName.valueToString();
    }

    public boolean isThis() {
        return getName().isThis();
    }

    protected void setParentCopy(final @NotNull Variable parent) {
        this.myParentCopy = parent;
    }

    protected void cutChildCopy() {
        myChildCopy = null;
    }

    protected Variable createVariable(final int index, final @NotNull Type type, final @NotNull String name) {
        return new Variable(index, type, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return equalsWithChildren(o) || equalsWithParents(o);
    }

    private boolean equalsWithParents(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return myParentCopy != null && myParentCopy.equalsWithParents(o);
    }

    private boolean equalsWithChildren(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return myChildCopy != null && myChildCopy.equalsWithChildren(o);
    }

    @Override
    public int hashCode() {
        return myIndex;   //todo correct
    }

    public boolean isUndefined() {
        return myName.isNull();
    }

    @Override
    public boolean findVariable(final @NotNull Variable variable) {
        return equals(variable);
    }
}
