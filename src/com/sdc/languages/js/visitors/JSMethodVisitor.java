package com.sdc.languages.js.visitors;

import com.sdc.languages.general.languageParts.Method;
import com.sdc.languages.general.visitors.GeneralMethodVisitor;
import com.sdc.languages.js.languageParts.JSLanguagePartFactory;
import com.sdc.util.DeclarationWorker;

public class JSMethodVisitor extends GeneralMethodVisitor {
    public JSMethodVisitor(final Method method, final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName) {
        super(method, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);

        this.myLanguagePartFactory = new JSLanguagePartFactory();
        this.myVisitorFactory = new JSVisitorFactory();
        this.myLanguage = DeclarationWorker.SupportedLanguage.JAVASCRIPT;
    }

    @Override
    protected boolean checkForAutomaticallyGeneratedAnnotation(final String annotationName) {
        return false;
    }
}
