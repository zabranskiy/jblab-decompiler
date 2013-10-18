package com.sdc.languages.java.languageParts;

import JavaPrinters.JavaPrinter;
import com.sdc.languages.general.astUtils.Frame;
import com.sdc.languages.general.languageParts.Method;
import com.sdc.languages.java.astUtils.JavaFrame;
import pretty.PrettyPackage;

import com.sdc.languages.general.languageParts.GeneralClass;

import java.util.List;

public class JavaMethod extends Method {
    public JavaMethod(final String modifier, final String returnType, final String name, final String signature, final String[] exceptions,
                      final GeneralClass generalClass,
                      final List<String> genericTypes, final List<String> genericIdentifiers,
                      final int textWidth, final int nestSize)
    {
        super(modifier, returnType, name, signature, exceptions, generalClass, genericTypes, genericIdentifiers, textWidth, nestSize);
    }

    @Override
    protected String getInheritanceIdentifier() {
        return JavaClass.INHERITANCE_IDENTIFIER;
    }

    @Override
    protected int getParametersStartIndex() {
        return myModifier.contains("static") ? 0 : 1;
    }

    @Override
    public Frame createFrame() {
        return new JavaFrame();
    }

    @Override
    public String toString() {
          return PrettyPackage.pretty(myTextWidth, (new JavaPrinter()).printMethod(this));
    }
}
