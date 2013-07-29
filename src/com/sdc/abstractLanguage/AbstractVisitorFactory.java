package com.sdc.abstractLanguage;

public abstract class AbstractVisitorFactory {
    public abstract AbstractAnnotationVisitor createAnnotationVisitor(final AbstractAnnotation annotation);

    public abstract AbstractMethodVisitor createMethodVisitor(AbstractMethod abstractMethod, final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName);

    public abstract AbstractClassVisitor createClassVisitor(final int textWidth, final int nestSize);
}
