package com.sdc.kotlin;

import KotlinPrinter.KotlinPrinterPackage;
import pretty.PrettyPackage;

import com.sdc.abstractLanguage.AbstractClass;
import com.sdc.abstractLanguage.AbstractMethod;
import com.sdc.ast.expressions.Expression;

import java.util.Arrays;
import java.util.List;

public class KotlinClass extends AbstractClass {
    public final static String INHERITANCE_IDENTIFIER = ":";
    private final List<String> myTraits;

    private KotlinMethod myConstructor;
    private Expression mySuperClassConstructor;

    private boolean myIsNormalClass = true;

    public KotlinClass(final String modifier, final String type, final String name, final String packageName,
                       final List<String> traits, final String superClass,
                       final List<String> genericTypes, final List<String> genericIdentifiers,
                       final int textWidth, final int nestSize)
    {
        super(modifier, type, name, packageName, traits, superClass, genericTypes, genericIdentifiers, textWidth, nestSize);
        this.myTraits = traits;
        this.myDefaultPackages = Arrays.asList(myPackage, "java.lang", "jet", "jet.runtime");
    }

    public List<String> getTraits() {
        return myTraits;
    }

    public void setIsNormalClass(boolean isNormalClass) {
        myIsNormalClass = isNormalClass;
    }

    public void setConstructor(final KotlinMethod constructor) {
        this.myConstructor = constructor;
    }

    public void setSuperClassConstructor(final Expression superClassConstructor) {
        this.mySuperClassConstructor = superClassConstructor;
    }

    public AbstractMethod getConstructor() {
        return myConstructor;
    }

    public Expression getSuperClassConstructor() {
        return mySuperClassConstructor;
    }

    public boolean isNormalClass() {
        return myIsNormalClass;
    }

    protected String getInheritanceIdentifier() {
        return INHERITANCE_IDENTIFIER;
    }

    @Override
    public String toString() {
        return PrettyPackage.pretty(myTextWidth, KotlinPrinterPackage.printKotlinClass(this));
    }
}
