package com.sdc.cfg.constructions;

public abstract class Construction {
    protected Construction myNextConstruction;

    protected String myLabel;
    protected String myBreak;
    protected String myContinue;

    public Construction getNextConstruction() {
        return myNextConstruction;
    }

    public void setNextConstruction(final Construction nextConstruction) {
        this.myNextConstruction = nextConstruction;
    }

    public boolean hasNextConstruction() {
        return myNextConstruction != null;
    }

    public String getBreak() {
        return myBreak;
    }

    public void setBreak(final String breakLabel) {
        this.myBreak = breakLabel;
    }

    public String getContinue() {
        return myContinue;
    }

    public void setContinues(final String continueLabel) {
        this.myContinue = continueLabel;
    }

    public String getLabel() {
        return myLabel;
    }

    public void setLabel(final String label) {
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
