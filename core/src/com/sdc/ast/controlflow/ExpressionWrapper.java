package com.sdc.ast.controlflow;

import com.sdc.ast.expressions.Expression;

import org.jetbrains.annotations.NotNull;


public interface ExpressionWrapper {
    @NotNull
    public Expression toExpression();
}
