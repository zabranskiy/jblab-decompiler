package com.sdc.java;

import JavaPrinter.JavaPrinterPackage;
import pretty.PrettyPackage;

import com.sdc.abstractLanguage.AbstractClass;
import com.sdc.abstractLanguage.AbstractMethod;

import java.util.List;

public class JavaMethod extends AbstractMethod {
    public JavaMethod(final String modifier, final String returnType, final String name, final String[] exceptions,
                      final AbstractClass abstractClass,
                      final List<String> genericTypes, final List<String> genericIdentifiers,
                      final int textWidth, final int nestSize)
    {
        super(modifier, returnType, name, exceptions, abstractClass, genericTypes, genericIdentifiers, textWidth, nestSize);
        this.myRootAbstractFrame = new JavaFrame();
        this.myCurrentAbstractFrame = myRootAbstractFrame;
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
    public String toString() {
          return PrettyPackage.pretty(myTextWidth, JavaPrinterPackage.printClassMethod(this));
    }
}
