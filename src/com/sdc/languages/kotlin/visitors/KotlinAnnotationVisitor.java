package com.sdc.languages.kotlin.visitors;

import com.sdc.languages.general.languageParts.AbstractAnnotation;
import com.sdc.languages.general.visitors.AbstractAnnotationVisitor;

public class KotlinAnnotationVisitor extends AbstractAnnotationVisitor {
    public KotlinAnnotationVisitor(final AbstractAnnotation annotation) {
        super(annotation);
    }
}
