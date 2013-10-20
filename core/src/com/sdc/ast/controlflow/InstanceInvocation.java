package com.sdc.ast.controlflow;

import org.jetbrains.annotations.NotNull;


public class InstanceInvocation extends Invocation {
    public InstanceInvocation(final @NotNull com.sdc.ast.expressions.InstanceInvocation invocation) {
        super(invocation);
    }
}
