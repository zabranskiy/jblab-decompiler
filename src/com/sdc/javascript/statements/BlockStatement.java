package com.sdc.javascript.statements;

import java.util.List;

public class BlockStatement extends Statement {
    public final List<Statement> body;

    public BlockStatement(final List<Statement> body) {
        this.body = body;
    }
}
