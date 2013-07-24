package com.decompiler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Language {
    private String name;
    private static final Map<String, String> myMap;

    static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("Java", ".java");
        aMap.put("JavaScript", ".js");
        myMap = Collections.unmodifiableMap(aMap);
    }

    public Language(String name) {
        this.name = name;
    }

    public String getExtension() {
        return myMap.get(name);
    }

    public String getName() {
        return name;
    }
}
