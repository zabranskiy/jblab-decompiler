package com.sdc.languages.java.visitors;

import com.sdc.languages.general.languageParts.Method;
import com.sdc.languages.general.visitors.GeneralMethodVisitor;
import com.sdc.languages.java.languageParts.JavaLanguagePartFactory;

import com.sdc.util.*;

import org.jetbrains.annotations.NotNull;


public class JavaMethodVisitor extends GeneralMethodVisitor {
    public JavaMethodVisitor(final @NotNull Method method,
                             final @NotNull String decompiledOwnerFullClassName,
                             final @NotNull String decompiledOwnerSuperClassName) {
        super(method, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);

        this.myLanguagePartFactory = new JavaLanguagePartFactory();
        this.myVisitorFactory = new JavaVisitorFactory();
        this.myLanguage = DeclarationWorker.SupportedLanguage.JAVA;
    }

    @Override
    protected boolean checkForAutomaticallyGeneratedAnnotation(final @NotNull String annotationName) {
        return false;
    }
}
