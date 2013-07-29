package com.sdc.java;

import com.sdc.abstractLanguage.AbstractClassVisitor;

public class JavaClassVisitor extends AbstractClassVisitor {
    private static final String DEFAULT_EXTENDED_CLASS = "java/lang/Object";

    public JavaClassVisitor(final int textWidth, final int nestSize) {
        super(textWidth, nestSize);
        this.myLanguagePartFactory = new JavaLanguagePartFactory();
        this.myVisitorFactory = new JavaVisitorFactory();
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