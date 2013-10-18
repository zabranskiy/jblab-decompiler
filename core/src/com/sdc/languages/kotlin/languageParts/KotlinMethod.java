package com.sdc.languages.kotlin.languageParts;

import KotlinPrinters.KotlinPrinter;
import com.sdc.languages.general.astUtils.Frame;
import com.sdc.languages.general.languageParts.Method;
import com.sdc.languages.kotlin.astUtils.KotlinFrame;
import pretty.PrettyPackage;

import com.sdc.languages.general.languageParts.GeneralClass;

import java.util.List;

public class KotlinMethod extends Method {
    private boolean hasReceiverParameter = false;

    public KotlinMethod(final String modifier, final String returnType, final String name, final String signature, final String[] exceptions,
                      final GeneralClass generalClass,
                      final List<String> genericTypes, final List<String> genericIdentifiers,
                      final int textWidth, final int nestSize)
    {
        super(modifier, returnType, name, signature, exceptions, generalClass, genericTypes, genericIdentifiers, textWidth, nestSize);
    }

    @Override
    protected String getInheritanceIdentifier() {
        return KotlinClass.INHERITANCE_IDENTIFIER;
    }

    @Override
    protected int getParametersStartIndex() {
        return myModifier.contains("static") ? 0 : isNormalClassMethod() || hasReceiverParameter ? 1 : 0;
    }

    @Override
    public Frame createFrame() {
        return new KotlinFrame();
    }

    public void dragReceiverFromMethodParameters() {
        hasReceiverParameter = true;
        declareThisVariable();
        myName = getCurrentFrame().getVariable(0).getType() + "." + myName;
    }

    public boolean isNormalClassMethod() {
        return myGeneralClass.isNormalClass();
    }

    @Override
    public String toString() {
        return PrettyPackage.pretty(myTextWidth, (new KotlinPrinter()).printMethod(this));
    }
}
