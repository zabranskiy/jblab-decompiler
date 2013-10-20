package com.sdc.ast.expressions.identifiers;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.PriorityExpression;

import org.jetbrains.annotations.NotNull;


public abstract class Identifier extends PriorityExpression {
    public Identifier(final @NotNull ExpressionType expressionType, final @NotNull Type type) {
        super(expressionType, type);
    }

    @NotNull
    abstract public Expression getName();
}
