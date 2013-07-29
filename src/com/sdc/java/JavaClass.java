package com.sdc.java;

import pretty.PrettyPackage;
import JavaPrinter.JavaPrinterPackage;

import com.sdc.abstractLanguage.AbstractClass;

import java.util.Arrays;
import java.util.List;

public class JavaClass extends AbstractClass {
    public final static String INHERITANCE_IDENTIFIER = "extends";

    public JavaClass(final String modifier, final String type, final String name, final String packageName,
            final List<String> implementedInterfaces, final String superClass,
            final List<String> genericTypes, final List<String> genericIdentifiers,
            final int textWidth, final int nestSize)
    {
        super(modifier, type, name, packageName, implementedInterfaces, superClass, genericTypes, genericIdentifiers, textWidth, nestSize);
        this.myDefaultPackages = Arrays.asList(myPackage, "java.lang");
    }

    @Override
    protected String getInheritanceIdentifier() {
        return INHERITANCE_IDENTIFIER;
    }

    @Override
    public String toString() {
        return PrettyPackage.pretty(myTextWidth, JavaPrinterPackage.printJavaClass(this));
    }
}
