package com.sdc.ast.controlflow;

import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.identifiers.Identifier;

public class Assignment extends Statement {
    private final Identifier myLeft;
    private final Expression myRight;

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
}
