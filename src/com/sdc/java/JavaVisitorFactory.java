package com.sdc.java;

import com.sdc.abstractLanguage.*;

public class JavaVisitorFactory extends AbstractVisitorFactory {
    public AbstractAnnotationVisitor createAnnotationVisitor(final AbstractAnnotation annotation) {
        return new JavaAnnotationVisitor(annotation);
    }

    public AbstractMethodVisitor createMethodVisitor(AbstractMethod abstractMethod
            , final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName)
    {
        return new JavaMethodVisitor(abstractMethod, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);
    }

    public AbstractClassVisitor createClassVisitor(final int textWidth, final int nestSize) {
        return new JavaClassVisitor(textWidth, nestSize);
    }
}
