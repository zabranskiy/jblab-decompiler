package com.sdc.languages.general.languageParts;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


public class Annotation {
    protected String myName;
    protected Map<String, Object> myProperties = new HashMap<String, Object>();

    @Nullable
    public String getName() {
        return myName;
    }

    public void setName(final @NotNull String name) {
        this.myName = name.trim();
    }

    @NotNull
    public Map<String, Object> getProperties() {
        return myProperties;
    }

    public void addProperty(final @NotNull String name, final @NotNull Object value) {
        myProperties.put(name, value);
    }

    public boolean isStringProperty(final @NotNull String name) {
        return myProperties.get(name) instanceof String;
    }
}
