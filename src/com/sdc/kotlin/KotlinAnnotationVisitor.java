package com.sdc.kotlin;

import com.sdc.abstractLanguage.AbstractAnnotation;
import com.sdc.abstractLanguage.AbstractAnnotationVisitor;

public class KotlinAnnotationVisitor extends AbstractAnnotationVisitor {
    public KotlinAnnotationVisitor(final AbstractAnnotation annotation) {
        super(annotation);
    }
}
