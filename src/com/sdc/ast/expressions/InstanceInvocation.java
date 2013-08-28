package com.sdc.ast.expressions;

import com.sdc.ast.Type;

import java.util.List;

public class InstanceInvocation extends Invocation {
    private final Expression myInstance;
    private boolean myIsNotNullCheckedCall;

    public Expression getInstance() {
        return myInstance;
    }

    public InstanceInvocation(final String function, final Type type, final List<Expression> arguments, final Expression instance) {
        super(function, type, arguments);
        this.myInstance = instance;
    }

    public boolean isNotNullCheckedCall() {
        return myIsNotNullCheckedCall;
    }

    public void setIsNotNullCheckedCall(final boolean isNotNullCheckedCall) {
        this.myIsNotNullCheckedCall = isNotNullCheckedCall;
    }

    @Override
    public Expression getBase() {
        if (myInstance instanceof Invocation || myInstance instanceof Cast) {
            return myInstance.getBase();
        } else {
            return this;
        }
    }

}
