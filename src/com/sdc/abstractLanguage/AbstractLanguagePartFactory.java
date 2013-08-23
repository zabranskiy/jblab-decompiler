package com.sdc.abstractLanguage;

import java.util.List;

public abstract class AbstractLanguagePartFactory {
    public abstract AbstractClass createClass(final String modifier, final AbstractClass.ClassType type, final String name, final String packageName,
                                              final List<String> implementedInterfaces, final String superClass,
                                              final List<String> genericTypes, final List<String> genericIdentifiers,
                                              final int textWidth, final int nestSize);

    public abstract AbstractMethod createMethod(final String modifier, final String returnType, final String name, final String signature, final String[] exceptions,
                                                final AbstractClass abstractClass, final List<String> genericTypes, final List<String> genericIdentifiers,
                                                final int textWidth, final int nestSize);

    public abstract AbstractAnnotation createAnnotation();

    public abstract AbstractClassField createClassField(final String modifier, final String type, final String name, final int textWidth, final int nestSize);

    public abstract AbstractFrame createFrame();
}
