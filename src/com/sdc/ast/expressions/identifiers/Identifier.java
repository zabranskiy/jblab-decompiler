package com.sdc.ast.expressions.identifiers;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.PriorityExpression;

public abstract class Identifier extends PriorityExpression {
    public Identifier(ExpressionType expressionType, Type type) {
        super(expressionType, type);
    }

    abstract public Expression getName();
}
