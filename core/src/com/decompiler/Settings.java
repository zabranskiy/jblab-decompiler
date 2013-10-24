package com.decompiler;

public class Settings {

    private static Settings instance;
    private String myPathToCurrentProject;
    private boolean myEnableGD;

    private Settings() {
    }

    public static Settings getInstance() {
        return instance = (instance == null) ? new Settings() : instance;
    }

    public String getPath() {
        return myPathToCurrentProject;
    }

    public void setPath(String path) {
        this.myPathToCurrentProject = path;
    }

    public void enableGraphDrawer() {
        this.myEnableGD = true;
    }

    public boolean isGraphDrawerEnable() {
        return myEnableGD;
    }
}
