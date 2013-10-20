package com.sdc.languages.kotlin.visitors;

import com.sdc.languages.general.languageParts.Annotation;
import com.sdc.languages.general.visitors.GeneralAnnotationVisitor;

import org.jetbrains.annotations.NotNull;


public class KotlinAnnotationVisitor extends GeneralAnnotationVisitor {
    public KotlinAnnotationVisitor(final @NotNull Annotation annotation) {
        super(annotation);
    }
}
