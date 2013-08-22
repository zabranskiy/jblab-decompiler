package com.sdc.ast.expressions;

public class Special extends Expression {
    private Expression myInnerExpression;

    public Expression getExpression() {
        return myInnerExpression;
    }

    public void setExpression(Expression myInnerExpression) {
        this.myInnerExpression = myInnerExpression;
    }

    public boolean isDefined() {
        return myInnerExpression != null;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }
}
