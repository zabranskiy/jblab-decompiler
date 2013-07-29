package com.sdc.kotlin;

import com.sdc.abstractLanguage.*;

public class KotlinVisitorFactory extends AbstractVisitorFactory {
    public AbstractAnnotationVisitor createAnnotationVisitor(final AbstractAnnotation annotation) {
        return new KotlinAnnotationVisitor(annotation);
    }

    public AbstractMethodVisitor createMethodVisitor(AbstractMethod abstractMethod
            , final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName)
    {
        return new KotlinMethodVisitor(abstractMethod, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);
    }

    public AbstractClassVisitor createClassVisitor(final int textWidth, final int nestSize) {
        return new KotlinClassVisitor(textWidth, nestSize);
    }
}
