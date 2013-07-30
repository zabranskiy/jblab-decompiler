package com.sdc.kotlin;

import KotlinPrinter.KotlinPrinter;
import pretty.PrettyPackage;

import com.sdc.abstractLanguage.AbstractClass;
import com.sdc.abstractLanguage.AbstractMethod;
import com.sdc.ast.expressions.Expression;

import java.util.Arrays;
import java.util.List;

public class KotlinClass extends AbstractClass {
    public final static String INHERITANCE_IDENTIFIER = ":";

    private AbstractMethod myConstructor;
    private Expression mySuperClassConstructor;

    public KotlinClass(final String modifier, final String type, final String name, final String packageName,
                       final List<String> traits, final String superClass,
                       final List<String> genericTypes, final List<String> genericIdentifiers,
                       final int textWidth, final int nestSize)
    {
        super(modifier, type, name, packageName, traits, superClass, genericTypes, genericIdentifiers, textWidth, nestSize);
        this.myDefaultPackages = Arrays.asList(myPackage, "java.lang", "jet", "jet.runtime");
    }

    public void setConstructor(final AbstractMethod constructor) {
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

    protected String getInheritanceIdentifier() {
        return INHERITANCE_IDENTIFIER;
    }

    protected void removeMethod(final AbstractMethod abstractMethod) {
        myMethods.remove(abstractMethod);
    }

    @Override
    public String toString() {
        return PrettyPackage.pretty(myTextWidth, (new KotlinPrinter()).printClass(this));
    }
}
