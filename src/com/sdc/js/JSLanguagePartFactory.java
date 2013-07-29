package com.sdc.js;

import com.sdc.abstractLanguage.*;
import com.sdc.java.JavaAnnotation;

import java.util.List;

public class JSLanguagePartFactory extends AbstractLanguagePartFactory {
    public AbstractClass createClass(final String modifier, final String type, final String name, final String packageName,
                                     final List<String> implementedInterfaces, final String superClass,
                                     final List<String> genericTypes, final List<String> genericIdentifiers,
                                     final int textWidth, final int nestSize)
    {
        return new JSClass(modifier, type, name, packageName, implementedInterfaces, superClass, genericTypes, genericIdentifiers, textWidth, nestSize);
    }

    public AbstractMethod createMethod(final String modifier, final String returnType, final String name, final String[] exceptions,
                                       final AbstractClass abstractClass, final List<String> genericTypes, final List<String> genericIdentifiers,
                                       final int textWidth, final int nestSize)
    {
        return new JSClassMethod(modifier, returnType, name, exceptions, abstractClass, genericTypes, genericIdentifiers, textWidth, nestSize);
    }

    public AbstractAnnotation createAnnotation() {
        return new JavaAnnotation();
    }

    public AbstractClassField createClassField(final String modifier, final String type,
                                               final String name, final int textWidth, final int nestSize)
    {
        return new JSClassField(modifier, type, name, textWidth, nestSize);
    }
}
