package com.sdc.ast.expressions;

import com.sdc.ast.Type;
import com.sdc.ast.expressions.nestedclasses.NestedClass;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class InstanceInvocation extends Invocation {
    private final Expression myInstance;
    private boolean myIsNotNullCheckedCall;

    public InstanceInvocation(final @NotNull String function,
                              final @NotNull Type type,
                              final @NotNull List<Expression> arguments,
                              final @NotNull Expression instance) {
        super(function, type, arguments);
        this.myInstance = instance;
    }

    @NotNull
    public Expression getInstance() {
        return myInstance;
    }

    public boolean isNotNullCheckedCall() {
        return myIsNotNullCheckedCall;
    }

    public void setIsNotNullCheckedCall(final boolean isNotNullCheckedCall) {
        this.myIsNotNullCheckedCall = isNotNullCheckedCall;
    }

    @NotNull
    @Override
    public Expression getBase() {
        if (myInstance instanceof Invocation || myInstance instanceof Cast) {
            return myInstance.getBase();
        } else {
            return this;
        }
    }

    @NotNull
    public List<InstanceInvocation> getInstanceInvocationSequence() {
        final List<InstanceInvocation> instanceInvocations = new ArrayList<InstanceInvocation>();
        if (myInstance instanceof InstanceInvocation) {
            instanceInvocations.addAll(((InstanceInvocation) myInstance).getInstanceInvocationSequence());
        }
        instanceInvocations.add(this);
        return instanceInvocations;
    }

    @NotNull
    public List<InstanceInvocation> getInstanceInvocationSequenceExceptThis() {
        final List<InstanceInvocation> instanceInvocations = getInstanceInvocationSequence();
        instanceInvocations.remove(instanceInvocations.size() - 1);
        return instanceInvocations;
    }

    //return empty list is not correct to transform to String summary
    //else return Expressions to sum in straight order
    @NotNull
    public List<Expression> getAppendSequenceExpressions() {
        final List<Expression> appendExpressions = new ArrayList<Expression>();

        if (isToString()) {
            final List<InstanceInvocation> instanceInvocations = getInstanceInvocationSequenceExceptThis();
            if (instanceInvocations.size() == 0) {
                return appendExpressions;
            }

            final InstanceInvocation lastInvocation = instanceInvocations.get(0);
            final Expression base = lastInvocation.getInstance();
            if (base instanceof New && ((New) base).getConstructor().getFunction().equals("StringBuilder")) {
                for (final InstanceInvocation invocation : instanceInvocations) {
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

    @NotNull
    @Override
    public List<Expression> getSubExpressions() {
        final List<Expression> subExpressions = new ArrayList<Expression>();
        subExpressions.add(myInstance);
        subExpressions.addAll(getArguments());
        return subExpressions;
    }

    @Override
    public boolean hasNotStaticInvocations() {
        boolean res = super.hasNotStaticInvocations();
        if (!(myInstance instanceof NestedClass)) {
            res = true;
        }
        return res;
    }
}
