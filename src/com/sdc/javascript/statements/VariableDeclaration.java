package com.sdc.javascript.statements;

import com.sdc.javascript.expressions.Expression;

public class VariableDeclaration extends Statement {
    public enum DeclarationType {
        VAR, CONST
    }

    public final DeclarationType type;
    public final String name;
    public final Expression init;

    public VariableDeclaration(final String name) {
        this.type = DeclarationType.VAR;
        this.name = name;
        this.init = null;
    }

    public VariableDeclaration(final DeclarationType type, final String name, final Expression init) {
        this.type = type;
        this.name = name;
        this.init = init;
    }
}
