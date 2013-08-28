package com.sdc.languages.general.visitors;

import com.sdc.languages.general.languageParts.AbstractAnnotation;
import com.sdc.languages.general.languageParts.AbstractMethod;
import com.sdc.languages.general.visitors.AbstractAnnotationVisitor;
import com.sdc.languages.general.visitors.AbstractClassVisitor;
import com.sdc.languages.general.visitors.AbstractMethodVisitor;

public abstract class AbstractVisitorFactory {
    public abstract AbstractAnnotationVisitor createAnnotationVisitor(final AbstractAnnotation annotation);

    public abstract AbstractMethodVisitor createMethodVisitor(AbstractMethod abstractMethod, final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName);

    public abstract AbstractClassVisitor createClassVisitor(final int textWidth, final int nestSize);
}
