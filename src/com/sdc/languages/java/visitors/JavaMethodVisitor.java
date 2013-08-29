package com.sdc.languages.java.visitors;

import com.sdc.languages.general.languageParts.Method;
import com.sdc.languages.general.visitors.GeneralMethodVisitor;
import com.sdc.languages.java.languageParts.JavaLanguagePartFactory;
import com.sdc.util.*;

public class JavaMethodVisitor extends GeneralMethodVisitor {
    public JavaMethodVisitor(final Method method, final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName) {
        super(method, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);

        this.myLanguagePartFactory = new JavaLanguagePartFactory();
        this.myVisitorFactory = new JavaVisitorFactory();
        this.myLanguage = DeclarationWorker.SupportedLanguage.JAVA;
    }

    @Override
    protected boolean checkForAutomaticallyGeneratedAnnotation(final String annotationName) {
        return false;
    }
}
