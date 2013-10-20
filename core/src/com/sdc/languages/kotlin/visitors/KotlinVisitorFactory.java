package com.sdc.languages.kotlin.visitors;

import com.sdc.languages.general.languageParts.Annotation;
import com.sdc.languages.general.languageParts.Method;
import com.sdc.languages.general.visitors.GeneralAnnotationVisitor;
import com.sdc.languages.general.visitors.GeneralClassVisitor;
import com.sdc.languages.general.visitors.GeneralMethodVisitor;
import com.sdc.languages.general.visitors.GeneralVisitorFactory;

import org.jetbrains.annotations.NotNull;


public class KotlinVisitorFactory extends GeneralVisitorFactory {
    @NotNull
    @Override
    public GeneralAnnotationVisitor createAnnotationVisitor(final @NotNull Annotation annotation) {
        return new KotlinAnnotationVisitor(annotation);
    }

    @NotNull
    @Override
    public GeneralMethodVisitor createMethodVisitor(final @NotNull Method method,
                                                    final @NotNull String decompiledOwnerFullClassName,
                                                    final @NotNull String decompiledOwnerSuperClassName) {
        return new KotlinMethodVisitor(method, decompiledOwnerFullClassName, decompiledOwnerSuperClassName);
    }

    @NotNull
    @Override
    public GeneralClassVisitor createClassVisitor(final int textWidth, final int nestSize) {
        return new KotlinClassVisitor(textWidth, nestSize);
    }
}
