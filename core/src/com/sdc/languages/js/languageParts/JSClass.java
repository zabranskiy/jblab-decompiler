package com.sdc.languages.js.languageParts;

import JSPrinters.JSPrinter;
import pretty.PrettyPackage;

import com.sdc.languages.general.languageParts.GeneralClass;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;


public class JSClass extends GeneralClass {
    public final static String INHERITANCE_IDENTIFIER = "extends";

    public JSClass(final @NotNull String modifier,
                   final @NotNull ClassType type,
                   final @NotNull String name,
                   final @NotNull String packageName,
                   final @NotNull List<String> implementedInterfaces,
                   final @NotNull String superClass,
                   final @NotNull List<String> genericTypes,
                   final @NotNull List<String> genericIdentifiers,
                   final int textWidth,
                   final int nestSize) {
        super(modifier, type, name, packageName, implementedInterfaces, superClass
                , genericTypes, genericIdentifiers, textWidth, nestSize);
        this.myDefaultPackages = Arrays.asList(myPackage);
    }

    @NotNull
    @Override
    protected String getInheritanceIdentifier() {
        return INHERITANCE_IDENTIFIER;
    }

    @NotNull
    @Override
    public String toString() {
        return PrettyPackage.pretty(myTextWidth, (new JSPrinter()).printClass(this));
    }
}
