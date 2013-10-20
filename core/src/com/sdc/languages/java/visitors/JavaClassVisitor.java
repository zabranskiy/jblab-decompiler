package com.sdc.languages.java.visitors;

import com.sdc.languages.general.visitors.GeneralClassVisitor;
import com.sdc.languages.java.languageParts.JavaLanguagePartFactory;
import com.sdc.util.DeclarationWorker;

import org.jetbrains.annotations.NotNull;


public class JavaClassVisitor extends GeneralClassVisitor {
    private static final String DEFAULT_EXTENDED_CLASS = "java/lang/Object";

    public JavaClassVisitor(final int textWidth, final int nestSize) {
        super(textWidth, nestSize);

        this.myLanguagePartFactory = new JavaLanguagePartFactory();
        this.myVisitorFactory = new JavaVisitorFactory();
        this.myLanguage = DeclarationWorker.SupportedLanguage.JAVA;
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