package com.sdc.ast.expressions.identifiers;

import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.PriorityExpression;

public abstract class Identifier extends PriorityExpression {
    //abstract public String getName();
    abstract public Expression getName();

    abstract public String getType();
}
