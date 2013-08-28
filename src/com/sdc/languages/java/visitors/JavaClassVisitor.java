package com.sdc.languages.java.visitors;

import com.sdc.languages.general.visitors.AbstractClassVisitor;
import com.sdc.languages.java.languageParts.JavaLanguagePartFactory;
import com.sdc.util.DeclarationWorker;

public class JavaClassVisitor extends AbstractClassVisitor {
    private static final String DEFAULT_EXTENDED_CLASS = "java/lang/Object";

    public JavaClassVisitor(final int textWidth, final int nestSize) {
        super(textWidth, nestSize);

        this.myLanguagePartFactory = new JavaLanguagePartFactory();
        this.myVisitorFactory = new JavaVisitorFactory();
        this.myLanguage = DeclarationWorker.SupportedLanguage.JAVA;
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