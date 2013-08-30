package com.sdc.ast.expressions.nestedclasses;

import com.sdc.ast.Type;
import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.identifiers.Variable;
import com.sdc.languages.general.languageParts.GeneralClass;

public class NestedClass extends Expression {
    protected final GeneralClass myNestedClass;

    public NestedClass(final GeneralClass nestedClass) {
        super(new Type(nestedClass.getName()));
        this.myNestedClass = nestedClass;
    }

    public GeneralClass getNestedClass() {
        return myNestedClass;
    }

    @Override
    public boolean findVariable(Variable variable) {
        return false;
    }
}
