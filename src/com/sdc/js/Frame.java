package com.sdc.js;

import com.sdc.abstractLangauge.AbstractFrame;

import java.util.*;

public class Frame implements AbstractFrame {
    private String myStackedVariableType = "";
    private int myStackedVariableIndex = 0;
    private boolean myStackChecked = false;

    private Map<Integer, String> myLocalVariableNames = new HashMap<Integer, String>();
    private Map<Integer, String> myLocalVariableTypes = new HashMap<Integer, String>();
    private List<Integer> myDeclaredVariables = new ArrayList<Integer>();
    private Set<Integer> myLocalVariablesFromDebugInfo = new HashSet<Integer>();

    private Frame myParent = null;
    private List<Frame> myChildren = new ArrayList<Frame>();
    private Frame mySameFrame = null;

    public String getStackedVariableType() {
        return myStackedVariableType;
    }

    public void setStackedVariableType(String stackedVariableType) {
        this.myStackedVariableType = stackedVariableType;
    }

    public void setStackedVariableIndex(int stackedVariableIndex) {
        this.myStackedVariableIndex = stackedVariableIndex;
    }

    public int getStackedVariableIndex() {
        return myStackedVariableIndex;
    }

    public Frame getParent() {
        return myParent;
    }

    public void setParent(final Frame parent) {
        this.myParent = parent;
    }

    public void setSameFrame(final Frame sameFrame) {
        this.mySameFrame = sameFrame;
    }

    public boolean checkStack() {
        if (!myStackChecked && !myStackedVariableType.isEmpty()) {
            myStackChecked = true;
            return true;
        }
        return false;
    }

    public boolean hasStack() {
        return !myStackedVariableType.isEmpty();
    }

    public void addChild(final Frame child) {
        myChildren.add(child);
    }

    public void addLocalVariableName(final int index, final String name) {
        myLocalVariableNames.put(index, name);
    }

    public void addLocalVariableType(final int index, final String type) {
        myLocalVariableTypes.put(index, type);
    }

    public boolean addLocalVariableFromDebugInfo(final int index, final String name, final String type) {
        if (!containsIndex(index) || myLocalVariablesFromDebugInfo.contains(index)) {
            for (final Frame frame : myChildren) {
                if (frame.addLocalVariableFromDebugInfo(index, name, type)) {
                    return true;
                }
            }
            return false;
        } else {
            myLocalVariablesFromDebugInfo.add(index);
            addLocalVariableName(index, name);
            addLocalVariableType(index, type);
            return true;
        }
    }

    public String getLocalVariableName(final int index) {
        if (containsIndex(index)) {
            if (myDeclaredVariables.contains(index)) {
                return myLocalVariableNames.get(index);
            } else {
                myDeclaredVariables.add(index);
                //return myLocalVariableTypes.get(index) + myLocalVariableNames.get(index);
                return myLocalVariableNames.get(index);
            }
        } else {
            if (mySameFrame == null) {
                return myParent.getLocalVariableName(index);
            } else {
                return mySameFrame.getLocalVariableName(index);
            }
        }
    }

    public boolean containsIndex(final int index) {
        return myLocalVariableTypes.containsKey(index);
    }
}
