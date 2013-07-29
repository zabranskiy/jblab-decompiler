package com.sdc.kotlin;

import com.sdc.abstractLanguage.*;

import java.util.List;

public class KotlinLanguagePartFactory extends AbstractLanguagePartFactory {
    public AbstractClass createClass(final String modifier, final String type, final String name, final String packageName,
                                     final List<String> implementedInterfaces, final String superClass,
                                     final List<String> genericTypes, final List<String> genericIdentifiers,
                                     final int textWidth, final int nestSize)
    {
        return new KotlinClass(modifier, type, name, packageName, implementedInterfaces, superClass, genericTypes, genericIdentifiers, textWidth, nestSize);
    }

    public AbstractMethod createMethod(final String modifier, final String returnType, final String name, final String[] exceptions,
                                       final AbstractClass abstractClass, final List<String> genericTypes, final List<String> genericIdentifiers,
                                       final int textWidth, final int nestSize)
    {
        return new KotlinMethod(modifier, returnType, name, exceptions, abstractClass, genericTypes, genericIdentifiers, textWidth, nestSize);
    }

    public AbstractAnnotation createAnnotation() {
        return new KotlinAnnotation();
    }

    public AbstractClassField createClassField(final String modifier, final String type,
                                               final String name, final int textWidth, final int nestSize)
    {
        return new KotlinClassField(modifier, type, name, textWidth, nestSize);
    }
}
