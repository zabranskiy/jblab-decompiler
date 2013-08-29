package com.sdc.ast.expressions;

import com.sdc.ast.Type;

import java.util.ArrayList;
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

    public List<InstanceInvocation> getInstanceInvocationSequence() {
        List<InstanceInvocation> instanceInvocations = new ArrayList<InstanceInvocation>();
        if (myInstance instanceof InstanceInvocation) {
            instanceInvocations.addAll(((InstanceInvocation) myInstance).getInstanceInvocationSequence());
        }
        instanceInvocations.add(this);
        return instanceInvocations;
    }

    public List<InstanceInvocation> getInstanceInvocationSequenceExceptThis() {
        List<InstanceInvocation> instanceInvocations = getInstanceInvocationSequence();
        instanceInvocations.remove(instanceInvocations.size()-1);
        return instanceInvocations;
    }

    //return empty list is not correct to transform to String summary
    //else return Expressions to sum in straight order
    public List<Expression> getAppendSequenceExpressions() {
        List<Expression> appendExpressions = new ArrayList<Expression>();
        if (isToString()) {
            List<InstanceInvocation> instanceInvocations = getInstanceInvocationSequenceExceptThis();
            int size = instanceInvocations.size();
            InstanceInvocation lastInvocation = instanceInvocations.get(0);
            Expression base = lastInvocation.getInstance();
            if (base instanceof New && ((New) base).getConstructor().getFunction().equals("StringBuilder")) {
                for (InstanceInvocation invocation : instanceInvocations) {
                    if (!invocation.getFunction().equals("append") || invocation.getArguments().size() != 1) {
                        appendExpressions.clear();
                        break;
                    }
                    appendExpressions.add(invocation.getArguments().get(0));
                }
            }
        }
        return appendExpressions;
    }

    public boolean isToString() {
        return getFunction().equals("toString");
    }

}
