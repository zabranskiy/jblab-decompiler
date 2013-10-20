package com.sdc.languages.general.visitors;

import com.sdc.languages.general.languageParts.Annotation;
import com.sdc.languages.general.languageParts.Method;

import org.jetbrains.annotations.NotNull;


public abstract class GeneralVisitorFactory {
    @NotNull
    public abstract GeneralAnnotationVisitor createAnnotationVisitor(final @NotNull Annotation annotation);

    @NotNull
    public abstract GeneralMethodVisitor createMethodVisitor(final @NotNull Method method,
                                                             final @NotNull String decompiledOwnerFullClassName,
                                                             final @NotNull String decompiledOwnerSuperClassName);

    @NotNull
    public abstract GeneralClassVisitor createClassVisitor(final int textWidth, final int nestSize);
}
