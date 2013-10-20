package com.sdc.ast.controlflow;

import com.sdc.ast.expressions.Expression;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class Return extends Statement {
    private Expression myReturnValue;
    private boolean myNeedToPrintReturn = true;

    public Return() {
        this.myReturnValue = null;
    }

    public Return(final @NotNull Expression returnValue) {
        this.myReturnValue = returnValue;
    }

    @Nullable
    public Expression getReturnValue() {
        return myReturnValue;
    }

    public void setReturnValue(final @Nullable Expression returnValue) {
        this.myReturnValue = returnValue;
    }

    public boolean needToPrintReturn() {
        return myNeedToPrintReturn;
    }

    public void setNeedToPrintReturn(final boolean needToPrintReturn) {
        this.myNeedToPrintReturn = needToPrintReturn;
    }
}
