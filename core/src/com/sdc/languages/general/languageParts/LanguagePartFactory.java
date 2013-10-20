package com.sdc.languages.general.languageParts;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public abstract class LanguagePartFactory {
    @NotNull
    public abstract GeneralClass createClass(final @NotNull String modifier,
                                             final @NotNull GeneralClass.ClassType type,
                                             final @NotNull String name,
                                             final @NotNull String packageName,
                                             final @NotNull List<String> implementedInterfaces,
                                             final @NotNull String superClass,
                                             final @NotNull List<String> genericTypes,
                                             final @NotNull List<String> genericIdentifiers,
                                             final int textWidth,
                                             final int nestSize);

    @NotNull
    public abstract Method createMethod(final @NotNull String modifier,
                                        final @NotNull String returnType,
                                        final @NotNull String name,
                                        final @NotNull String signature,
                                        final @Nullable String[] exceptions,
                                        final @NotNull GeneralClass generalClass,
                                        final @NotNull List<String> genericTypes,
                                        final @NotNull List<String> genericIdentifiers,
                                        final int textWidth,
                                        final int nestSize);

    @NotNull
    public abstract Annotation createAnnotation();

    @NotNull
    public abstract ClassField createClassField(final @NotNull String modifier,
                                                final @NotNull String type,
                                                final @NotNull String name,
                                                final int textWidth,
                                                final int nestSize);
}
