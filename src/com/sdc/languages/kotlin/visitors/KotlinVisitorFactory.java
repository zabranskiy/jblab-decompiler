package com.sdc.languages.kotlin.visitors;

import com.sdc.languages.general.languageParts.AbstractAnnotation;
import com.sdc.languages.general.languageParts.AbstractMethod;
import com.sdc.languages.general.visitors.AbstractAnnotationVisitor;
import com.sdc.languages.general.visitors.AbstractClassVisitor;
import com.sdc.languages.general.visitors.AbstractMethodVisitor;
import com.sdc.languages.general.visitors.AbstractVisitorFactory;

public class KotlinVisitorFactory extends AbstractVisitorFactory {
    @Override
    public AbstractAnnotationVisitor createAnnotationVisitor(final AbstractAnnotation annotation) {
        return new KotlinAnnotationVisitor(annotation);
    }

    @Override
    public AbstractMethodVisitor createMethodVisitor(AbstractMethod abstractMethod
            , final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName)
    {
        return new KotlinMethodVisitor(abstractMethod, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);
    }

    @Override
    public AbstractClassVisitor createClassVisitor(final int textWidth, final int nestSize) {
        return new KotlinClassVisitor(textWidth, nestSize);
    }
}
