package com.sdc.javascript;


import com.sdc.abstractLanguage.AbstractClassField;
import com.sdc.javascript.expressions.Expression;
import com.sdc.javascript.expressions.Literal;
//import pretty.PrettyPackage;

public class JSClassField extends AbstractClassField{
    private final String myModifier;
    private final String myType;
    private final String myName;
    private final Expression myDefaultValue;

    private final int myTextWidth;
    private final int myNestSize;

    public JSClassField(final String modifier, final String type, final String name, final int textWidth, final int nestSize) {
        this.myModifier = modifier;
        this.myType = type;
        this.myName = name;
        this.myTextWidth = textWidth;
        this.myNestSize = nestSize;

        this.myDefaultValue = new Literal("0");
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

    public Expression getDefaultValue() {
        return myDefaultValue;
    }

    /*@Override
    public String toString() {
        return PrettyPackage.pretty(myTextWidth, JavaClassPrinterPackage.printClassField(this));
    } */
}
