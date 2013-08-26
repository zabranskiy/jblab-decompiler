package com.sdc.ast.controlflow;

import com.sdc.ast.expressions.Expression;

public class Return extends Statement {
    private Expression myReturnValue;
    private boolean myNeedToPrintReturn = true;

    public Return() {
        this.myReturnValue = null;
    }

    public Return(final Expression returnValue) {
        this.myReturnValue = returnValue;
    }

    public Expression getReturnValue() {
        return myReturnValue;
    }

    public void setReturnValue(final Expression returnValue) {
        this.myReturnValue = returnValue;
    }

    public boolean needToPrintReturn() {
        return myNeedToPrintReturn;
    }

    public void setNeedToPrintReturn(final boolean needToPrintReturn) {
        this.myNeedToPrintReturn = needToPrintReturn;
    }
}
