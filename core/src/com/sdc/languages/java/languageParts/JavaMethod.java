package com.sdc.languages.java.languageParts;

import JavaPrinters.JavaPrinter;
import pretty.PrettyPackage;

import com.sdc.languages.general.astUtils.Frame;
import com.sdc.languages.general.languageParts.Method;
import com.sdc.languages.java.astUtils.JavaFrame;
import com.sdc.languages.general.languageParts.GeneralClass;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class JavaMethod extends Method {
    public JavaMethod(final @NotNull String modifier,
                      final @NotNull String returnType,
                      final @NotNull String name,
                      final @NotNull String signature,
                      final String[] exceptions,
                      final @NotNull GeneralClass generalClass,
                      final @NotNull List<String> genericTypes,
                      final @NotNull List<String> genericIdentifiers,
                      final int textWidth,
                      final int nestSize) {
        super(modifier, returnType, name, signature, exceptions, generalClass
                , genericTypes, genericIdentifiers, textWidth, nestSize);
    }

    @NotNull
    @Override
    protected String getInheritanceIdentifier() {
        return JavaClass.INHERITANCE_IDENTIFIER;
    }

    @Override
    protected int getParametersStartIndex() {
        return myModifier.contains("static") ? 0 : 1;
    }

    @NotNull
    @Override
    public Frame createFrame() {
        return new JavaFrame();
    }

    @NotNull
    @Override
    public String toString() {
        return PrettyPackage.pretty(myTextWidth, (new JavaPrinter()).printMethod(this));
    }
}
