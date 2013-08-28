package com.sdc.ast.expressions.nestedclasses;

import com.sdc.languages.general.languageParts.AbstractClass;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.identifiers.Variable;

public class NestedClass extends Expression {
    protected final AbstractClass myNestedClass;

    public NestedClass(final AbstractClass nestedClass) {
        super(new Type(nestedClass.getName()));
        this.myNestedClass = nestedClass;
    }

    public AbstractClass getNestedClass() {
        return myNestedClass;
    }

    @Override
    public boolean findVariable(Variable variable) {
        return false;
    }
}
