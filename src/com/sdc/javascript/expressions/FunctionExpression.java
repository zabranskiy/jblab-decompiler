package com.sdc.javascript.expressions;

import com.sdc.javascript.statements.Statement;

import java.util.List;

public class FunctionExpression extends Expression {
    public final List<String> params;
    public final Statement body;

    public FunctionExpression (final List<String> params, final Statement body){
        this.params = params;
        this.body = body;
    }
}
