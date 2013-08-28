package com.sdc.languages.java.visitors;

import com.sdc.languages.general.languageParts.AbstractAnnotation;
import com.sdc.languages.general.visitors.AbstractAnnotationVisitor;

public class JavaAnnotationVisitor extends AbstractAnnotationVisitor {
    public JavaAnnotationVisitor(final AbstractAnnotation javaAnnotation) {
        super(javaAnnotation);
    }
}
