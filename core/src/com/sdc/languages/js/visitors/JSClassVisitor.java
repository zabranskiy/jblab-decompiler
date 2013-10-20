package com.sdc.languages.js.visitors;

import com.sdc.languages.general.visitors.GeneralClassVisitor;
import com.sdc.languages.js.languageParts.JSLanguagePartFactory;
import com.sdc.util.DeclarationWorker;

import org.jetbrains.annotations.NotNull;


public class JSClassVisitor extends GeneralClassVisitor {
    private static final String DEFAULT_EXTENDED_CLASS = "java/lang/Object";

    public JSClassVisitor(final int textWidth, final int nestSize) {
        super(textWidth, nestSize);

        this.myLanguagePartFactory = new JSLanguagePartFactory();
        this.myVisitorFactory = new JSVisitorFactory();
        this.myLanguage = DeclarationWorker.SupportedLanguage.JAVASCRIPT;
    }

    @NotNull
    @Override
    protected String getDefaultImplementedInterface() {
        return "";
    }

    @NotNull
    @Override
    protected String getDefaultExtendedClass() {
        return DEFAULT_EXTENDED_CLASS;
    }

    @Override
    protected boolean checkForAutomaticallyGeneratedAnnotation(final @NotNull String annotationName) {
        return false;
    }
}