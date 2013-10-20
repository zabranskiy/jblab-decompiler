package com.sdc.languages.java.languageParts;

import JavaPrinters.JavaPrinter;
import pretty.PrettyPackage;

import com.sdc.languages.general.languageParts.GeneralClass;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;


public class JavaClass extends GeneralClass {
    public final static String INHERITANCE_IDENTIFIER = "extends";

    public JavaClass(final @NotNull String modifier,
                     final @NotNull ClassType type,
                     final @NotNull String name,
                     final @NotNull String packageName,
                     final @NotNull List<String> implementedInterfaces,
                     final @NotNull String superClass,
                     final @NotNull List<String> genericTypes,
                     final @NotNull List<String> genericIdentifiers,
                     final int textWidth,
                     final int nestSize) {
        super(modifier, type, name, packageName, implementedInterfaces, superClass, genericTypes, genericIdentifiers, textWidth, nestSize);
        this.myDefaultPackages = Arrays.asList(myPackage, "java.lang");
    }

    @NotNull
    @Override
    protected String getInheritanceIdentifier() {
        return INHERITANCE_IDENTIFIER;
    }

    @NotNull
    @Override
    public String toString() {
        return PrettyPackage.pretty(myTextWidth, (new JavaPrinter()).printClass(this));
    }
}
