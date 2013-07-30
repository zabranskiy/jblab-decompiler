package com.sdc.java;

import com.sdc.abstractLanguage.*;
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
