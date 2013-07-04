package com.config;

import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

public class PluginComponent implements ApplicationComponent {
    public PluginComponent() {
    }

    public void initComponent() {
        // nop
    }

    public void disposeComponent() {
        // nop
    }

    @NotNull
    public String getComponentName() {
        return "PluginComponent";
    }

}
