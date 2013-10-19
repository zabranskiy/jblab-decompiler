package com.decompiler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Language for ease of use in the decompiler.
 * Dmitriy Zabranskiy, 2013
 */
public class Language {
    private static final Map<String, String> myLanguageToExtensionMap;

    private String myName;

    static {
        final Map<String, String> languageToExtensionMap = new HashMap<String, String>();
        languageToExtensionMap.put("Java", ".java");
        languageToExtensionMap.put("JavaScript", ".js");
        languageToExtensionMap.put("Kotlin", ".kt");
        myLanguageToExtensionMap = Collections.unmodifiableMap(languageToExtensionMap);
    }

    public Language(final @NotNull String name) {
        this.myName = name;
    }

    @Nullable
    public String getExtension() {
        return myLanguageToExtensionMap.get(myName);
    }

    @NotNull
    public String getName() {
        return myName;
    }
}
