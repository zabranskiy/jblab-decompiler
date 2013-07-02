package com.sdc.javascript.statements;

import com.sdc.javascript.expressions.Expression;

import java.util.List;

public class Switch extends Statement {
    public final Expression expr;
    public final List<SwitchCase> cases;

    public Switch(final Expression expr, final List<SwitchCase> cases) {
        this.expr = expr;
        this.cases = cases;
    }
}
