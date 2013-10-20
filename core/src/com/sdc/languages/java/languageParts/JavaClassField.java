package com.sdc.languages.java.languageParts;

import com.sdc.languages.general.languageParts.ClassField;

import org.jetbrains.annotations.NotNull;


public class JavaClassField extends ClassField {
    public JavaClassField(final @NotNull String modifier,
                          final @NotNull String type,
                          final @NotNull String name,
                          final int textWidth,
                          final int nestSize) {
        super(modifier, type, name, textWidth, nestSize);
    }
}
