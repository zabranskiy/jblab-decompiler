package com.sdc.languages.js.visitors;

import com.sdc.languages.general.languageParts.Method;
import com.sdc.languages.general.visitors.GeneralMethodVisitor;
import com.sdc.languages.js.languageParts.JSLanguagePartFactory;
import com.sdc.util.DeclarationWorker;

import org.jetbrains.annotations.NotNull;


public class JSMethodVisitor extends GeneralMethodVisitor {
    public JSMethodVisitor(final @NotNull Method method,
                           final @NotNull String decompiledOwnerFullClassName,
                           final @NotNull String decompiledOwnerSuperClassName) {
        super(method, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);

        this.myLanguagePartFactory = new JSLanguagePartFactory();
        this.myVisitorFactory = new JSVisitorFactory();
        this.myLanguage = DeclarationWorker.SupportedLanguage.JAVASCRIPT;
    }

    @Override
    protected boolean checkForAutomaticallyGeneratedAnnotation(final @NotNull String annotationName) {
        return false;
    }
}
