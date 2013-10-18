package com.sdc.languages.java.visitors;

import com.sdc.languages.general.languageParts.Annotation;
import com.sdc.languages.general.languageParts.Method;
import com.sdc.languages.general.visitors.GeneralAnnotationVisitor;
import com.sdc.languages.general.visitors.GeneralClassVisitor;
import com.sdc.languages.general.visitors.GeneralMethodVisitor;
import com.sdc.languages.general.visitors.GeneralVisitorFactory;

public class JavaVisitorFactory extends GeneralVisitorFactory {
    @Override
    public GeneralAnnotationVisitor createAnnotationVisitor(final Annotation annotation) {
        return new JavaAnnotationVisitor(annotation);
    }

    @Override
    public GeneralMethodVisitor createMethodVisitor(Method method
            , final String decompiledOwnerFullClassName, final String decompiledOwnerSuperClassName)
    {
        return new JavaMethodVisitor(method, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);
    }

    @Override
    public GeneralClassVisitor createClassVisitor(final int textWidth, final int nestSize) {
        return new JavaClassVisitor(textWidth, nestSize);
    }
}
