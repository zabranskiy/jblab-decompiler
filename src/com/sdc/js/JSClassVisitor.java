package com.sdc.js;

import com.sdc.abstractLanguage.AbstractClassVisitor;

public class JSClassVisitor extends AbstractClassVisitor {
    private static final String DEFAULT_EXTENDED_CLASS = "java/lang/Object";

    public JSClassVisitor(final int textWidth, final int nestSize) {
        super(textWidth, nestSize);
        this.myLanguagePartFactory = new JSLanguagePartFactory();
        this.myVisitorFactory = new JSVisitorFactory();
    }

    @Override
    protected String getDefaultImplementedInterface() {
        return "";
    }

    @Override
    protected String getDefaultExtendedClass() {
        return DEFAULT_EXTENDED_CLASS;
    }

    @Override
    protected boolean checkForAutomaticallyGeneratedAnnotation(final String annotationName) {
        return false;
    }
}