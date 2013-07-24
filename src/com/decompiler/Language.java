package com.decompiler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Language {
    private String myName;
    private static final Map<String, String> myLanguageToExtensionMap;

    static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("Java", ".java");
        aMap.put("JavaScript", ".js");
        aMap.put("Kotlin", ".kt");
        myLanguageToExtensionMap = Collections.unmodifiableMap(aMap);
    }

    public Language(final String name) {
        this.myName = name;
    }

    public String getExtension() {
        return myLanguageToExtensionMap.get(myName);
    }

    public String getName() {
        return myName;
    }
}
