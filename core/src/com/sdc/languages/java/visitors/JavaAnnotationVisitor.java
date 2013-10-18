package com.sdc.languages.java.visitors;

import com.sdc.languages.general.languageParts.Annotation;
import com.sdc.languages.general.visitors.GeneralAnnotationVisitor;

public class JavaAnnotationVisitor extends GeneralAnnotationVisitor {
    public JavaAnnotationVisitor(final Annotation javaAnnotation) {
        super(javaAnnotation);
    }
}
