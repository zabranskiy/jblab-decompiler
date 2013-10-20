package com.sdc.ast.expressions.identifiers;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.Constant;
import com.sdc.ast.expressions.Expression;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class Field extends Identifier {
    private final String myName;
    private String myStaticOwnerName;
    private Expression myOwner;

    public Field(final @NotNull String name, final @NotNull Type type) {
        super(ExpressionType.FIELD, type);
        this.myName = name;
    }

    public void setStaticOwnerName(final @NotNull String staticOwnerName) {
        this.myStaticOwnerName = staticOwnerName;
    }

    public void setOwner(final @NotNull Expression owner) {
        this.myOwner = owner;
    }

    @Nullable
    public String getStaticOwnerName() {
        return myStaticOwnerName;
    }

    @Nullable
    public Expression getOwner() {
        return myOwner;
    }

    @NotNull
    @Override
    public Expression getName() {
        return new Constant(myName, false, getType());
    }

    @Override
    public boolean findVariable(final @NotNull Variable variable) {
        return false;
    }
}
