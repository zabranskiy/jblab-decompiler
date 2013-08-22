package com.sdc.ast.expressions;

import com.sdc.ast.OperationType;
import com.sdc.cfg.constructions.Construction;

public class TernaryExpression extends PriorityExpression {
    private final Expression myCondition;
    private final Construction myLeft;
    private final Construction myRight;

    public TernaryExpression(Expression myCondition, Construction myLeft, Construction myRight) {
        this.myCondition = myCondition;
        this.myLeft = myLeft;
        this.myRight = myRight;
        myType = OperationType.TERNARY_IF;
    }

    public Construction getLeft() {
        return myLeft;
    }

    public Construction getRight() {
        return myRight;
    }

    public Expression getCondition() {
        return myCondition;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }
}
