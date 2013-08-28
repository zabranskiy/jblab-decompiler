package com.sdc.languages.js.languageParts;

import JSPrinters.JSPrinter;
import com.sdc.languages.general.astUtils.Frame;
import com.sdc.languages.general.languageParts.Method;
import com.sdc.languages.js.astUtils.JSFrame;
import pretty.PrettyPackage;

import com.sdc.languages.general.languageParts.GeneralClass;

import java.util.List;

public class JSMethod extends Method {
    public JSMethod(final String modifier, final String returnType, final String name, final String signature, final String[] exceptions,
                    final GeneralClass generalClass,
                    final List<String> genericTypes, final List<String> genericIdentifiers,
                    final int textWidth, final int nestSize)
    {
        super(modifier, returnType, name, signature, exceptions, generalClass, genericTypes, genericIdentifiers, textWidth, nestSize);
    }

    @Override
    protected String getInheritanceIdentifier() {
        return JSClass.INHERITANCE_IDENTIFIER;
    }

    @Override
    protected int getParametersStartIndex() {
        return 1;
    }

    @Override
    public Frame createFrame() {
        return new JSFrame();
    }

    @Override
    public String toString() {
          return PrettyPackage.pretty(myTextWidth, (new JSPrinter()).printMethod(this));
    }
}
