package com.sdc.cfg.constructions;

import org.jetbrains.annotations.Nullable;


public abstract class Construction {
    protected Construction myNextConstruction;

    protected String myLabel;
    protected String myBreak;
    protected String myContinue;

    @Nullable
    public Construction getNextConstruction() {
        return myNextConstruction;
    }

    public void setNextConstruction(final @Nullable Construction nextConstruction) {
        this.myNextConstruction = nextConstruction;
    }

    public boolean hasNextConstruction() {
        return myNextConstruction != null;
    }

    @Nullable
    public String getBreak() {
        return myBreak;
    }

    public void setBreak(final @Nullable String breakLabel) {
        this.myBreak = breakLabel;
    }

    @Nullable
    public String getContinue() {
        return myContinue;
    }

    public void setContinue(final @Nullable String continueLabel) {
        this.myContinue = continueLabel;
    }

    @Nullable
    public String getLabel() {
        return myLabel;
    }

    public void setLabel(final @Nullable String label) {
        this.myLabel = label;
    }

    public boolean hasBreak() {
        return myBreak != null;
    }

    public boolean hasBreakToLabel() {
        return hasBreak() && !myBreak.isEmpty();
    }

    public boolean hasContinue() {
        return myContinue != null;
    }

    public boolean hasContinueToLabel() {
        return hasContinue() && !myContinue.isEmpty();
    }
}
