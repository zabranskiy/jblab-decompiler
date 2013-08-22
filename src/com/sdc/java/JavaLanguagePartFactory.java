package com.sdc.java;

import com.sdc.abstractLanguage.*;

import java.util.List;

public class JavaLanguagePartFactory extends AbstractLanguagePartFactory {
    @Override
    public AbstractClass createClass(final String modifier, final String type, final String name, final String packageName,
                                     final List<String> implementedInterfaces, final String superClass,
                                     final List<String> genericTypes, final List<String> genericIdentifiers,
                                     final int textWidth, final int nestSize)
    {
        return new JavaClass(modifier, type, name, packageName, implementedInterfaces, superClass, genericTypes, genericIdentifiers, textWidth, nestSize);
    }

    @Override
    public AbstractMethod createMethod(final String modifier, final String returnType, final String name, final String signature, final String[] exceptions,
                                       final AbstractClass abstractClass, final List<String> genericTypes, final List<String> genericIdentifiers,
                                       final int textWidth, final int nestSize)
    {
        return new JavaMethod(modifier, returnType, name, signature, exceptions, abstractClass, genericTypes, genericIdentifiers, textWidth, nestSize);
    }

    @Override
    public AbstractAnnotation createAnnotation() {
        return new JavaAnnotation();
    }

    @Override
    public AbstractClassField createClassField(final String modifier, final String type,
                                               final String name, final int textWidth, final int nestSize)
    {
        return new JavaClassField(modifier, type, name, textWidth, nestSize);
    }
}
