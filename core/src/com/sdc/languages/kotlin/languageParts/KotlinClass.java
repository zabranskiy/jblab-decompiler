package com.sdc.languages.kotlin.languageParts;

import KotlinPrinters.KotlinPrinter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pretty.PrettyPackage;

import com.sdc.languages.general.languageParts.Method;
import com.sdc.languages.general.languageParts.GeneralClass;
import com.sdc.ast.expressions.Expression;

import java.util.Arrays;
import java.util.List;


public class KotlinClass extends GeneralClass {
    public final static String INHERITANCE_IDENTIFIER = ":";

    private Method myConstructor;
    private Expression mySuperClassConstructor;
    private String mySrcClassName;

    public KotlinClass(final @NotNull String modifier,
                       final @NotNull ClassType type,
                       final @NotNull String name,
                       final @NotNull String packageName,
                       final @NotNull List<String> traits,
                       final @NotNull String superClass,
                       final @NotNull List<String> genericTypes,
                       final @NotNull List<String> genericIdentifiers,
                       final int textWidth,
                       final int nestSize) {
        super(modifier, type, name, packageName, traits, superClass, genericTypes, genericIdentifiers, textWidth, nestSize);
        this.myDefaultPackages = Arrays.asList(myPackage, "java.lang", "jet", "jet.runtime");
    }

    public void setConstructor(final @NotNull Method constructor) {
        this.myConstructor = constructor;
    }

    public void setSuperClassConstructor(final @NotNull Expression superClassConstructor) {
        this.mySuperClassConstructor = superClassConstructor;
    }

    @Nullable
    public Method getConstructor() {
        return myConstructor;
    }

    @Nullable
    public Expression getSuperClassConstructor() {
        return mySuperClassConstructor;
    }

    @NotNull
    protected String getInheritanceIdentifier() {
        return INHERITANCE_IDENTIFIER;
    }

    public void removeMethod(final @NotNull Method method) {
        myMethods.remove(method);
    }

    public void setSrcClassName(final @NotNull String srcClassName) {
        this.mySrcClassName = srcClassName;
    }

    @Nullable
    public String getSrcClassName() {
        return mySrcClassName;
    }

    public void addAssignmentToField(final @NotNull String fieldName) {
        ((KotlinClassField) getField(fieldName)).addAssignment();
    }

    @Override
    public void appendImport(final @NotNull String importName) {
        if (!importName.contains(".src.") && !importName.endsWith("KotlinPackage")) {
            super.appendImport(importName);
        }
    }

    @NotNull
    @Override
    public String getTypeToString() {
        switch (myType) {
            case INTERFACE:
                return "trait ";
            default:
                return super.getTypeToString();
        }
    }

    @NotNull
    @Override
    public String toString() {
        return PrettyPackage.pretty(myTextWidth, (new KotlinPrinter()).printClass(this));
    }
}
