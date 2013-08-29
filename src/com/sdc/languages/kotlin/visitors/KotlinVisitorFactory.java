package com.sdc.languages.kotlin.visitors;

import com.sdc.languages.general.languageParts.Annotation;
import com.sdc.languages.general.languageParts.Method;
import com.sdc.languages.general.visitors.GeneralAnnotationVisitor;
import com.sdc.languages.general.visitors.GeneralClassVisitor;
import com.sdc.languages.general.visitors.GeneralMethodVisitor;
import com.sdc.languages.general.visitors.GeneralVisitorFactory;

public class KotlinVisitorFactory extends GeneralVisitorFactory {
    @Override
    public GeneralAnnotationVisitor createAnnotationVisitor(final Annotation annotation) {
        return new KotlinAnnotationVisitor(annotation);
    }

    @Override
    public GeneralMethodVisitor createMethodVisitor(Method method
            , final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName)
    {
        return new KotlinMethodVisitor(method, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);
    }

    @Override
    public GeneralClassVisitor createClassVisitor(final int textWidth, final int nestSize) {
        return new KotlinClassVisitor(textWidth, nestSize);
    }
}
