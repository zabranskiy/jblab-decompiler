package com.sdc.languages.js.visitors;

import com.sdc.languages.general.languageParts.AbstractMethod;
import com.sdc.languages.general.visitors.AbstractMethodVisitor;
import com.sdc.languages.js.languageParts.JSLanguagePartFactory;
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
