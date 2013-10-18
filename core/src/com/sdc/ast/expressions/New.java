package com.sdc.ast.expressions;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;

public class New extends PriorityExpression {
    private final Invocation myConstructor;

    public New(final Invocation constructor) {
        super(ExpressionType.NEW,constructor==null?Type.VOID: new Type(constructor.getFunction()));
        this.myConstructor = constructor;
    }

    public Invocation getConstructor() {
        return myConstructor;
    }
    @Override
    public boolean findVariable(Variable variable) {
        return myConstructor.findVariable(variable);
    }
}
