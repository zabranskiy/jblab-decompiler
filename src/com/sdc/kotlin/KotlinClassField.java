package com.sdc.kotlin;

import com.sdc.abstractLanguage.AbstractClassField;
import com.sdc.ast.expressions.Expression;

public class KotlinClassField extends AbstractClassField {
    private final String myModifier;
    private final String myType;
    private final String myName;

    private Expression myInitializer;

    private final int myTextWidth;
    private final int myNestSize;

    public KotlinClassField(final String modifier, final String type, final String name, final int textWidth, final int nestSize) {
        this.myModifier = modifier;
        this.myType = type;
        this.myName = name;
        this.myTextWidth = textWidth;
        this.myNestSize = nestSize;
    }

    public String getModifier() {
        return myModifier;
    }

    public String getType() {
        return myType;
    }

    public String getName() {
        return myName;
    }

    public int getNestSize() {
        return myNestSize;
    }

    public void setInitializer(Expression initializer) {
        this.myInitializer = initializer;
    }

    public Expression getInitializer() {
        return myInitializer;
    }

    public boolean hasInitializer() {
        return myInitializer != null;
    }
}
