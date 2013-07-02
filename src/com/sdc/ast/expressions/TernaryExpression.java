package com.sdc.ast.expressions;

/**
 * Created with IntelliJ IDEA.
 * User: 1
 * Date: 04.05.13
 * Time: 15:47
 * To change this template use File | Settings | File Templates.
 */
public class TernaryExpression extends Expression {
    private final Expression myCondition;
    private final Expression myLeft;
    private final Expression myRight;

    public TernaryExpression(Expression myCondition, Expression myLeft, Expression myRight) {
        this.myCondition = myCondition;
        this.myLeft = myLeft;
        this.myRight = myRight;
    }

    public Expression getLeft() {
        return myLeft;
    }

    public Expression getRight() {
        return myRight;
    }

    public Expression getCondition() {
        return myCondition;
    }

}
