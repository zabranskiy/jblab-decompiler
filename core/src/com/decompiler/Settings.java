package com.decompiler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class Settings {
    private static Settings instance;
    private String myPathToCurrentProject;
    private boolean myEnableGraphDrawing;

    private Settings() {
    }

    @NotNull
    public static Settings getInstance() {
        return instance = (instance == null) ? new Settings() : instance;
    }

    @Nullable
    public String getPath() {
        return myPathToCurrentProject;
    }

    public void setPath(final @NotNull String path) {
        this.myPathToCurrentProject = path;
    }

    public void enableGraphDrawing() {
        this.myEnableGraphDrawing = true;
    }

    public boolean isGraphDrawingEnabled() {
        return myEnableGraphDrawing;
    }
}
