package com.sdc.kotlin;

import com.sdc.ast.expressions.Constant;
import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.identifiers.Variable;
import com.sdc.util.DeclarationWorker;

public class KotlinVariable extends Variable {
    private boolean myIsNotNull = false;

    public KotlinVariable(final int index, final String variableType, final String name) {
        super(index, variableType, name);
    }

    public boolean isNotNull() {
        return myIsNotNull;
    }

    public void setIsNotNull(final boolean isNotNull) {
        this.myIsNotNull = isNotNull;
    }

    @Override
    public Expression getName() {
        final String name = ((Constant) super.getName()).getValue().toString();
        final String actualName = myIsMethodParameter ? name : "var " + name;

        return myIsDeclared ? myName : new Constant(actualName, false);
    }

    @Override
    public String getType() {
        final boolean notNeedNullableMark = myIsNotNull || DeclarationWorker.isPrimitiveClass(myVariableType) || myVariableType.endsWith("?");
        final String nullableMark = notNeedNullableMark ? "" : "?";

        return super.getType() + nullableMark;
    }
}
