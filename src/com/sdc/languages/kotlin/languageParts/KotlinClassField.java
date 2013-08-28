package com.sdc.languages.kotlin.languageParts;

import com.sdc.languages.general.languageParts.AbstractClassField;

public class KotlinClassField extends AbstractClassField {
    private int myAssignmentsCount = 0;

    public KotlinClassField(final String modifier, final String type, final String name, final int textWidth, final int nestSize) {
        super(modifier, type, name, textWidth, nestSize);
    }

    public void addAssignment() {
        myAssignmentsCount++;
    }

    public boolean isMutable() {
        return myAssignmentsCount > 1;
    }

    @Override
    public String getName() {
        return myName;
    }
}
