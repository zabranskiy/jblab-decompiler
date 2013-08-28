package com.sdc.languages.java.visitors;

import com.sdc.languages.general.languageParts.AbstractMethod;
import com.sdc.languages.general.visitors.AbstractMethodVisitor;
import com.sdc.languages.java.languageParts.JavaLanguagePartFactory;
import com.sdc.util.*;

public class JavaMethodVisitor extends AbstractMethodVisitor {
    public JavaMethodVisitor(final AbstractMethod abstractMethod, final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName) {
        super(abstractMethod, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);

        this.myLanguagePartFactory = new JavaLanguagePartFactory();
        this.myVisitorFactory = new JavaVisitorFactory();
        this.myLanguage = DeclarationWorker.SupportedLanguage.JAVA;
    }

    @Override
    protected boolean checkForAutomaticallyGeneratedAnnotation(final String annotationName) {
        return false;
    }
}
