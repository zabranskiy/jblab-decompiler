package com.sdc.java;

import com.sdc.abstractLanguage.*;

public class JavaVisitorFactory extends AbstractVisitorFactory {
    @Override
    public AbstractAnnotationVisitor createAnnotationVisitor(final AbstractAnnotation annotation) {
        return new JavaAnnotationVisitor(annotation);
    }

    @Override
    public AbstractMethodVisitor createMethodVisitor(AbstractMethod abstractMethod
            , final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName)
    {
        return new JavaMethodVisitor(abstractMethod, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);
    }

    @Override
    public AbstractClassVisitor createClassVisitor(final int textWidth, final int nestSize) {
        return new JavaClassVisitor(textWidth, nestSize);
    }
}
