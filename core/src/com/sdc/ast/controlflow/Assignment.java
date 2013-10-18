package com.sdc.ast.controlflow;

import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.identifiers.Identifier;

public class Assignment extends Statement {
    private Identifier myLeft;
    private Expression myRight;

    public Assignment(final Identifier left, final Expression right) {
        this.myLeft = left;
        this.myRight = right;
    }

    public Identifier getLeft() {
        return myLeft;
    }

    public Expression getRight() {
        return myRight;
    }

    public void setLeft(final Identifier left) {
        this.myLeft = left;
    }

    public void setRight(final Expression right) {
        this.myRight = right;
    }
}
