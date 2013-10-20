package com.sdc.languages.java.visitors;

import com.sdc.languages.general.languageParts.Annotation;
import com.sdc.languages.general.visitors.GeneralAnnotationVisitor;

import org.jetbrains.annotations.NotNull;


public class JavaAnnotationVisitor extends GeneralAnnotationVisitor {
    public JavaAnnotationVisitor(final @NotNull Annotation javaAnnotation) {
        super(javaAnnotation);
    }
}
