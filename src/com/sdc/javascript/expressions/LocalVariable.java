package com.sdc.javascript.expressions;

import com.sdc.javascript.JSClassMethod;

public class LocalVariable {//extends Identifier {
    private final int myIndex;
    private final JSClassMethod myClassMethod;

    public LocalVariable(final int index, final JSClassMethod classMethod) {
        this.myIndex = index;
        this.myClassMethod = classMethod;
    }

    public String getName() {
        return myClassMethod.getLocalVariableName(myIndex);
    }
}
