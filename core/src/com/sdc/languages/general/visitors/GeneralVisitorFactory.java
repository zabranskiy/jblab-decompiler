package com.sdc.languages.general.visitors;

import com.sdc.languages.general.languageParts.Annotation;
import com.sdc.languages.general.languageParts.Method;

public abstract class GeneralVisitorFactory {
    public abstract GeneralAnnotationVisitor createAnnotationVisitor(final Annotation annotation);

    public abstract GeneralMethodVisitor createMethodVisitor(Method method, final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName);

    public abstract GeneralClassVisitor createClassVisitor(final int textWidth, final int nestSize);
}
