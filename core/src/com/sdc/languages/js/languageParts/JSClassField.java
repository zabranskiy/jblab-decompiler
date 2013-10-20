package com.sdc.languages.js.languageParts;

import com.sdc.languages.general.languageParts.ClassField;

import org.jetbrains.annotations.NotNull;


public class JSClassField extends ClassField {
    public JSClassField(final @NotNull String modifier,
                        final @NotNull String type,
                        final @NotNull String name,
                        final int textWidth,
                        final int nestSize) {
        super(modifier, type, name, textWidth, nestSize);
    }
}
