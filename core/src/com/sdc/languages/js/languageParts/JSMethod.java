package com.sdc.languages.js.languageParts;

import JSPrinters.JSPrinter;
import pretty.PrettyPackage;

import com.sdc.languages.general.astUtils.Frame;
import com.sdc.languages.general.languageParts.Method;
import com.sdc.languages.js.astUtils.JSFrame;
import com.sdc.languages.general.languageParts.GeneralClass;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class JSMethod extends Method {
    public JSMethod(final @NotNull String modifier,
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
        return JSClass.INHERITANCE_IDENTIFIER;
    }

    @Override
    protected int getParametersStartIndex() {
        return 1;
    }

    @NotNull
    @Override
    public Frame createFrame() {
        return new JSFrame();
    }

    @NotNull
    @Override
    public String toString() {
        return PrettyPackage.pretty(myTextWidth, (new JSPrinter()).printMethod(this));
    }
}
