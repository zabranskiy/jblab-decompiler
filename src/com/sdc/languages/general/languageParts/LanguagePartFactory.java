package com.sdc.languages.general.languageParts;

import java.util.List;

public abstract class LanguagePartFactory {
    public abstract GeneralClass createClass(final String modifier, final GeneralClass.ClassType type, final String name, final String packageName,
                                              final List<String> implementedInterfaces, final String superClass,
                                              final List<String> genericTypes, final List<String> genericIdentifiers,
                                              final int textWidth, final int nestSize);

    public abstract Method createMethod(final String modifier, final String returnType, final String name, final String signature, final String[] exceptions,
                                                final GeneralClass generalClass, final List<String> genericTypes, final List<String> genericIdentifiers,
                                                final int textWidth, final int nestSize);

    public abstract Annotation createAnnotation();

    public abstract ClassField createClassField(final String modifier, final String type, final String name, final int textWidth, final int nestSize);
}
