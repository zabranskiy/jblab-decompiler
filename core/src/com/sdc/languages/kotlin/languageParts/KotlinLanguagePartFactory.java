package com.sdc.languages.kotlin.languageParts;

import com.sdc.languages.general.languageParts.*;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class KotlinLanguagePartFactory extends LanguagePartFactory {
    @NotNull
    @Override
    public GeneralClass createClass(final @NotNull String modifier,
                                    final @NotNull GeneralClass.ClassType type,
                                    final @NotNull String name,
                                    final @NotNull String packageName,
                                    final @NotNull List<String> implementedInterfaces,
                                    final @NotNull String superClass,
                                    final @NotNull List<String> genericTypes,
                                    final @NotNull List<String> genericIdentifiers,
                                    final int textWidth,
                                    final int nestSize) {
        return new KotlinClass(modifier, type, name, packageName, implementedInterfaces, superClass
                , genericTypes, genericIdentifiers, textWidth, nestSize);
    }

    @NotNull
    @Override
    public Method createMethod(final @NotNull String modifier,
                               final @NotNull String returnType,
                               final @NotNull String name,
                               final @NotNull String signature,
                               final String[] exceptions,
                               final @NotNull GeneralClass generalClass,
                               final @NotNull List<String> genericTypes,
                               final @NotNull List<String> genericIdentifiers,
                               final int textWidth,
                               final int nestSize) {
        return new KotlinMethod(modifier, returnType, name, signature, exceptions, generalClass
                , genericTypes, genericIdentifiers, textWidth, nestSize);
    }

    @NotNull
    @Override
    public Annotation createAnnotation() {
        return new KotlinAnnotation();
    }

    @NotNull
    @Override
    public ClassField createClassField(final @NotNull String modifier,
                                       final @NotNull String type,
                                       final @NotNull String name,
                                       final int textWidth,
                                       final int nestSize) {
        return new KotlinClassField(modifier, type, name, textWidth, nestSize);
    }
}
