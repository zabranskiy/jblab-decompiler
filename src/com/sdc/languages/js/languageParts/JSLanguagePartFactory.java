package com.sdc.languages.js.languageParts;

import com.sdc.languages.general.languageParts.*;
import com.sdc.languages.java.languageParts.JavaAnnotation;

import java.util.List;

public class JSLanguagePartFactory extends LanguagePartFactory {
    @Override
    public GeneralClass createClass(final String modifier, final GeneralClass.ClassType type, final String name, final String packageName,
                                     final List<String> implementedInterfaces, final String superClass,
                                     final List<String> genericTypes, final List<String> genericIdentifiers,
                                     final int textWidth, final int nestSize)
    {
        return new JSClass(modifier, type, name, packageName, implementedInterfaces, superClass, genericTypes, genericIdentifiers, textWidth, nestSize);
    }

    @Override
    public Method createMethod(final String modifier, final String returnType, final String name, final String signature, final String[] exceptions,
                                       final GeneralClass generalClass, final List<String> genericTypes, final List<String> genericIdentifiers,
                                       final int textWidth, final int nestSize)
    {
        return new JSMethod(modifier, returnType, name, signature, exceptions, generalClass, genericTypes, genericIdentifiers, textWidth, nestSize);
    }

    @Override
    public Annotation createAnnotation() {
        return new JavaAnnotation();
    }

    @Override
    public ClassField createClassField(final String modifier, final String type,
                                               final String name, final int textWidth, final int nestSize)
    {
        return new JSClassField(modifier, type, name, textWidth, nestSize);
    }
}
