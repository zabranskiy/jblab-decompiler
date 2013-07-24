package com.sdc.abstractLanguage;

import java.util.HashMap;
import java.util.Map;

public class AbstractAnnotation {
    protected String myName;
    protected Map<String, Object> myProperties = new HashMap<String, Object>();

    public String getName() {
        return myName;
    }

    public void setName(String name) {
        this.myName = name.trim();
    }

    public Map<String, Object> getProperties() {
        return myProperties;
    }

    public void addProperty(final String name, final Object value) {
        myProperties.put(name, value);
    }

    public boolean isStringProperty(final String name) {
        return myProperties.get(name) instanceof String;
    }
}
