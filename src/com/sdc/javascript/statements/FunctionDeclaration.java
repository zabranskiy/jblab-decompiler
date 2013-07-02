package com.sdc.javascript.statements;

import java.util.List;
import com.sdc.javascript.expressions.Identifier;

public class FunctionDeclaration extends Statement {
    public final String name;
    public final List<Identifier> params;
    public final Statement body;

    public FunctionDeclaration (final String name, final List<Identifier> params, final Statement body){
        this.name = name;
        this.params = params;
        this.body = body;
    }
}
