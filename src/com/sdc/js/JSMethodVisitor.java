package com.sdc.js;

import com.sdc.abstractLanguage.AbstractMethod;
import com.sdc.abstractLanguage.AbstractMethodVisitor;
import com.sdc.util.DeclarationWorker;

public class JSMethodVisitor extends AbstractMethodVisitor {
    public JSMethodVisitor(final AbstractMethod abstractMethod, final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName) {
        super(abstractMethod, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);

        this.myLanguagePartFactory = new JSLanguagePartFactory();
        this.myVisitorFactory = new JSVisitorFactory();
        this.myLanguage = DeclarationWorker.SupportedLanguage.JAVASCRIPT;
    }

    @Override
    protected boolean checkForAutomaticallyGeneratedAnnotation(final String annotationName) {
        return false;
    }
}
