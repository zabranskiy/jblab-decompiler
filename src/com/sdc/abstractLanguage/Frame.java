package com.sdc.abstractLanguage;

import com.sdc.ast.expressions.identifiers.Variable;
import org.objectweb.asm.Label;

import java.util.*;

public class Frame {
    protected boolean myStackChecked = false;
    protected boolean myHasStack = false;

    protected Map<Integer, Integer> myVariableIndexToArrayPosition = new HashMap<Integer, Integer>();
    protected List<Variable> myVariables = new ArrayList<Variable>();

    protected Label myStart;
    protected Label myEnd;

    protected int myLastMethodParameterIndex = -1;
    protected int myLastCommonVariableIndexInList = -1;

    public boolean isMyStackChecked() {
        return myStackChecked;
    }

    public void setStackChecked(final boolean stackChecked) {
        this.myStackChecked = stackChecked;
    }

    public List<Variable> getVariables() {
        return myVariables;
    }

    public void setVariables(final List<Variable> variables) {
        this.myVariables = variables;
    }

    public Label getStart() {
        return myStart;
    }

    public void setStart(final Label start) {
        this.myStart = start;
    }

    public Label getEnd() {
        return myEnd;
    }

    public void setEnd(final Label end) {
        this.myEnd = end;
    }

    public int getLastMethodParameterIndex() {
        return myLastMethodParameterIndex;
    }

    public void setLastMethodParameterIndex(final int lastMethodParameterIndex) {
        this.myLastMethodParameterIndex = lastMethodParameterIndex;
    }

    public int getLastCommonVariableIndexInList() {
        return myLastCommonVariableIndexInList;
    }

    public void setLastCommonVariableIndexInList(int lastCommonVariableIndexInList) {
        this.myLastCommonVariableIndexInList = lastCommonVariableIndexInList;
    }

    public boolean hasStack() {
        return myHasStack;
    }

    public void setHasStack(final boolean hasStack) {
        this.myHasStack = hasStack;
    }

    public boolean checkStack() {
        if (!myStackChecked && !myHasStack) {
            myStackChecked = true;
            return true;
        }
        return false;
    }

    public void insertVariable(final int index, final String type, final String name) {
        if (!containsVariable(index)) {
            final Variable variable = new Variable(index, type, name);
            variable.setIsMethodParameter(myLastMethodParameterIndex < index);

            myVariables.add(variable);
            myVariableIndexToArrayPosition.put(index, myVariables.size());
        }
    }

    public void updateVariableInformation(final int index, final String type, final String name) {
        Variable variable = getVariable(index);
        variable.setVariableType(type);
        variable.setName(name);
    }

    public Variable getVariable(final int variableIndex) {
        return myVariables.get(myVariableIndexToArrayPosition.get(variableIndex));
    }

    public Frame createNextFrame(final int rightBound) {
        Frame newFrame = createFrame();

        newFrame.setVariables(getVariablesSubList(rightBound));
        newFrame.setLastCommonVariableIndexInList(rightBound - 1);
        newFrame.setLastMethodParameterIndex(myLastMethodParameterIndex);

        return newFrame;
    }

    public List<Variable> getMethodParameters() {
        return getVariablesSubList(myVariableIndexToArrayPosition.get(myLastMethodParameterIndex) + 1);
    }

    protected List<Variable> getVariablesSubList(final int rightBound) {
        return myVariables.subList(0, rightBound);
    }

    protected boolean containsVariable(final int index) {
        return myVariableIndexToArrayPosition.keySet().contains(index);
    }

    protected Frame createFrame() {
        return new Frame();
    }
}
