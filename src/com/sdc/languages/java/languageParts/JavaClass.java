package com.sdc.languages.java.languageParts;

import JavaPrinters.JavaPrinter;
import pretty.PrettyPackage;

import com.sdc.languages.general.languageParts.GeneralClass;

import java.util.Arrays;
import java.util.List;

public class JavaClass extends GeneralClass {
    public final static String INHERITANCE_IDENTIFIER = "extends";

    public JavaClass(final String modifier, final ClassType type, final String name, final String packageName,
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
        return PrettyPackage.pretty(myTextWidth, (new JavaPrinter()).printClass(this));
    }
}
