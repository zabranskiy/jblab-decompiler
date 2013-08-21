package com.sdc.ast.expressions;

import java.util.List;

public class InstanceInvocation extends Invocation {
    private final Expression myInstance;
    private boolean myIsNotNullCheckedCall;

    public Expression getInstance() {
        return myInstance;
    }

    public InstanceInvocation(final String function, final String returnType, final List<Expression> arguments, final Expression instance) {
        super(function, returnType, arguments);
        this.myInstance = instance;
    }

    public boolean isNotNullCheckedCall() {
        return myIsNotNullCheckedCall;
    }

    public void setIsNotNullCheckedCall(final boolean isNotNullCheckedCall) {
        this.myIsNotNullCheckedCall = isNotNullCheckedCall;
    }


}
