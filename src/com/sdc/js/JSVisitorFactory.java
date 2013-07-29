package com.sdc.js;

import com.sdc.abstractLanguage.*;
import com.sdc.java.JavaAnnotationVisitor;

public class JSVisitorFactory extends AbstractVisitorFactory {
    @Override
    public AbstractAnnotationVisitor createAnnotationVisitor(final AbstractAnnotation annotation) {
        return new JavaAnnotationVisitor(annotation);
    }

    @Override
    public AbstractMethodVisitor createMethodVisitor(AbstractMethod abstractMethod
            , final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName)
    {
        return new JSMethodVisitor(abstractMethod, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);
    }

    @Override
    public AbstractClassVisitor createClassVisitor(final int textWidth, final int nestSize) {
        return new JSClassVisitor(textWidth, nestSize);
    }
}
