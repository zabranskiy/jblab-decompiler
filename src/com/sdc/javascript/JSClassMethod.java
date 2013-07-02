package com.sdc.javascript;

import com.sdc.abstractLangauge.AbstractClassMethod;
import com.sdc.javascript.statements.Statement;
import com.sdc.cfg.Edge;
import com.sdc.cfg.GraphDrawer;
import com.sdc.cfg.Node;
//import pretty.PrettyPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSClassMethod extends AbstractClassMethod {
    private final String myModifier;
    private final String myReturnType;
    private final String myName;
    private final String[] myExceptions;

    private final int myLastLocalVariableIndex;
    private Map<Integer, String> myLocalVariableNames = new HashMap<Integer, String>();
    private Map<Integer, String> myLocalVariableTypes = new HashMap<Integer, String>();
    private List<Integer> myDeclaredVariables = new ArrayList<Integer>();

    private List<Statement> myBody = null;

    private List<Node> myNodes = null;
    private List<Edge> myEdges = null;

    private final int myTextWidth;
    private final int myNestSize;

    public String getModifier() {
        return myModifier;
    }

    public String getReturnType() {
        return myReturnType;
    }

    public String getName() {
        return myName;
    }

    public String[] getExceptions() {
        return myExceptions;
    }

    public int getLastLocalVariableIndex() {
        return myLastLocalVariableIndex;
    }

    public int getNestSize() {
        return myNestSize;
    }

    public List<Statement> getBody() {
        return myBody;
    }

    public void setBody(final List<Statement> body) {
        this.myBody = body;
    }

    public JSClassMethod(final String modifier, final String returnType, final String name, final String[] exceptions,
                         final int lastLocalVariableIndex, final int textWidth, final int nestSize) {
        this.myModifier = modifier;
        this.myReturnType = returnType;
        this.myName = name;
        this.myExceptions = exceptions;
        this.myLastLocalVariableIndex = lastLocalVariableIndex;
        this.myTextWidth = textWidth;
        this.myNestSize = nestSize;
    }

    public void addLocalVariableName(final int index, final String name) {
        myLocalVariableNames.put(index, name);
    }

    public void addLocalVariableType(final int index, final String type) {
        myLocalVariableTypes.put(index, type);
    }

    public void clearCollectedLocalVaribles() {
        myLocalVariableNames.clear();
        myLocalVariableTypes.clear();
    }

    public String getLocalVariableName(final int index) {
        if (myDeclaredVariables.contains(index)) {
            return myLocalVariableNames.get(index);
        } else {
            myDeclaredVariables.add(index);
            return myLocalVariableTypes.get(index) + myLocalVariableNames.get(index);
        }
    }

    public List<String> getParameters() {
        List<String> parameters = new ArrayList<String>();
        for (int variableIndex = 1; variableIndex <= myLastLocalVariableIndex; variableIndex++) {
            if (myLocalVariableNames.containsKey(variableIndex)) {
                parameters.add(getLocalVariableName(variableIndex));
            }
        }
        return parameters;
    }

    public void setNodes(List<Node> myNodes) {
        this.myNodes = myNodes;
    }

    public void setEdges(List<Edge> myEdges) {
        this.myEdges = myEdges;
    }

    public void drawCFG() {
        if (myEdges.size() > 2) {
            GraphDrawer graphDrawer = new GraphDrawer(myNodes, myNestSize, myTextWidth);
            graphDrawer.draw();
            graphDrawer.simplyDraw();
        }
    }

    /*@Override
    public String toString() {
        return PrettyPackage.pretty(myTextWidth, JavaClassPrinterPackage.printClassMethod(this));
    }*/
}
