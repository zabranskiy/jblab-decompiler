package com.sdc.ast.controlflow;

import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.identifiers.Identifier;

import org.jetbrains.annotations.NotNull;


public class Assignment extends Statement {
    private Identifier myLeft;
    private Expression myRight;

    public Assignment(final @NotNull Identifier left, final @NotNull Expression right) {
        this.myLeft = left;
        this.myRight = right;
    }

    @NotNull
    public Identifier getLeft() {
        return myLeft;
    }

    @NotNull
    public Expression getRight() {
        return myRight;
    }

    public void setLeft(final @NotNull Identifier left) {
        this.myLeft = left;
    }

    public void setRight(final @NotNull Expression right) {
        this.myRight = right;
    }
}
