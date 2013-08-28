package com.sdc.languages.kotlin.visitors;

import com.sdc.languages.general.languageParts.Annotation;
import com.sdc.languages.general.visitors.GeneralAnnotationVisitor;

public class KotlinAnnotationVisitor extends GeneralAnnotationVisitor {
    public KotlinAnnotationVisitor(final Annotation annotation) {
        super(annotation);
    }
}
