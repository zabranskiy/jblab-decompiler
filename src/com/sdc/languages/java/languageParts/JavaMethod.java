package com.sdc.languages.java.languageParts;

import JavaPrinters.JavaPrinter;
import com.sdc.languages.general.astUtils.AbstractFrame;
import com.sdc.languages.java.astUtils.JavaFrame;
import pretty.PrettyPackage;

import com.sdc.languages.general.languageParts.AbstractClass;
import com.sdc.languages.general.languageParts.AbstractMethod;

import java.util.List;

public class JavaMethod extends AbstractMethod {
    public JavaMethod(final String modifier, final String returnType, final String name, final String signature, final String[] exceptions,
                      final AbstractClass abstractClass,
                      final List<String> genericTypes, final List<String> genericIdentifiers,
                      final int textWidth, final int nestSize)
    {
        super(modifier, returnType, name, signature, exceptions, abstractClass, genericTypes, genericIdentifiers, textWidth, nestSize);
    }

    @Override
    protected String getInheritanceIdentifier() {
        return JavaClass.INHERITANCE_IDENTIFIER;
    }

    @Override
    protected int getParametersStartIndex() {
        return 1;
    }

    @Override
    public AbstractFrame createFrame() {
        return new JavaFrame();
    }

    @Override
    public String toString() {
          return PrettyPackage.pretty(myTextWidth, (new JavaPrinter()).printMethod(this));
    }
}
