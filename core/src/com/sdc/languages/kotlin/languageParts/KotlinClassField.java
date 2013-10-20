package com.sdc.languages.kotlin.languageParts;

import com.sdc.languages.general.languageParts.ClassField;

import org.jetbrains.annotations.NotNull;


public class KotlinClassField extends ClassField {
    private int myAssignmentsCount = 0;

    public KotlinClassField(final @NotNull String modifier,
                            final @NotNull String type,
                            final @NotNull String name,
                            final int textWidth,
                            final int nestSize) {
        super(modifier, type, name, textWidth, nestSize);
    }

    public void addAssignment() {
        myAssignmentsCount++;
    }

    public boolean isMutable() {
        return myAssignmentsCount > 1;
    }

    @NotNull
    @Override
    public String getName() {
        return myName;
    }
}
