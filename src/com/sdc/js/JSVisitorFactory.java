package com.sdc.js;

import com.sdc.abstractLanguage.*;
import com.sdc.java.JavaAnnotationVisitor;

public class JSVisitorFactory extends AbstractVisitorFactory {
    public AbstractAnnotationVisitor createAnnotationVisitor(final AbstractAnnotation annotation) {
        return new JavaAnnotationVisitor(annotation);
    }

    public AbstractMethodVisitor createMethodVisitor(AbstractMethod abstractMethod
            , final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName)
    {
        return new JSMethodVisitor(abstractMethod, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);
    }

    public AbstractClassVisitor createClassVisitor(final int textWidth, final int nestSize) {
        return new JSClassVisitor(textWidth, nestSize);
    }
}
