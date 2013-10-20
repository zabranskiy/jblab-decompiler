package com.sdc.ast.expressions.nestedclasses;

import com.sdc.ast.Type;
import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.identifiers.Variable;
import com.sdc.languages.general.languageParts.GeneralClass;

import org.jetbrains.annotations.NotNull;


public class NestedClass extends Expression {
    protected final GeneralClass myNestedClass;

    public NestedClass(final @NotNull GeneralClass nestedClass) {
        super(new Type(nestedClass.getName()));
        this.myNestedClass = nestedClass;
    }

    @NotNull
    public GeneralClass getNestedClass() {
        return myNestedClass;
    }

    @Override
    public boolean findVariable(final @NotNull Variable variable) {
        return false;
    }
}
