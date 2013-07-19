package com.sdc.java;

import JavaClassPrinter.JavaClassPrinterPackage;
import pretty.PrettyPackage;

import com.sdc.abstractLanguage.AbstractClassField;

public class JavaClassField  extends AbstractClassField{
    private final String myModifier;
    private final String myType;
    private final String myName;

    private final int myTextWidth;
    private final int myNestSize;

    public JavaClassField(final String modifier, final String type, final String name, final int textWidth, final int nestSize) {
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

    @Override
    public String toString() {
        return PrettyPackage.pretty(myTextWidth, JavaClassPrinterPackage.printClassField(this));
    }
}
