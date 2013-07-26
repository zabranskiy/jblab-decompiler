package com.sdc.ast.expressions.nestedclasses;

import com.sdc.abstractLanguage.AbstractClass;
import com.sdc.ast.expressions.Expression;

public class NestedClass extends Expression {
    protected final AbstractClass myNestedClass;

    public NestedClass(final AbstractClass nestedClass) {
        this.myNestedClass = nestedClass;
    }

    public AbstractClass getNestedClass() {
        return myNestedClass;
    }
}
