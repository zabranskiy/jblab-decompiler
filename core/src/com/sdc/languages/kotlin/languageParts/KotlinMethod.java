package com.sdc.languages.kotlin.languageParts;

import KotlinPrinters.KotlinPrinter;
import pretty.PrettyPackage;

import com.sdc.languages.general.languageParts.GeneralClass;
import com.sdc.languages.general.astUtils.Frame;
import com.sdc.languages.general.languageParts.Method;
import com.sdc.languages.kotlin.astUtils.KotlinFrame;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class KotlinMethod extends Method {
    private boolean hasReceiverParameter = false;

    public KotlinMethod(final @NotNull String modifier,
                        final @NotNull String returnType,
                        final @NotNull String name,
                        final @NotNull String signature,
                        final String[] exceptions,
                        final @NotNull GeneralClass generalClass,
                        final @NotNull List<String> genericTypes,
                        final @NotNull List<String> genericIdentifiers,
                        final int textWidth,
                        final int nestSize)
    {
        super(modifier, returnType, name, signature, exceptions, generalClass
                , genericTypes, genericIdentifiers, textWidth, nestSize);
    }

    @NotNull
    @Override
    protected String getInheritanceIdentifier() {
        return KotlinClass.INHERITANCE_IDENTIFIER;
    }

    @Override
    protected int getParametersStartIndex() {
        return myModifier.contains("static") ? 0 : isNormalClassMethod() || hasReceiverParameter ? 1 : 0;
    }

    @NotNull
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

    @NotNull
    @Override
    public String toString() {
        return PrettyPackage.pretty(myTextWidth, (new KotlinPrinter()).printMethod(this));
    }
}
