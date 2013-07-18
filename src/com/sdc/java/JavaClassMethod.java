package com.sdc.java;

import JavaClassPrinter.JavaClassPrinterPackage;
import pretty.PrettyPackage;

import com.sdc.abstractLangauge.AbstractClassMethod;
import com.sdc.ast.controlflow.Statement;
import com.sdc.cfg.GraphDrawer;
import com.sdc.cfg.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaClassMethod extends AbstractClassMethod {
    private final String myModifier;
    private final String myReturnType;
    private final String myName;
    private final String[] myExceptions;

    private List<String> myImports = new ArrayList<String>();

    private final JavaClass myJavaClass;
    private final List<String> myGenericTypes;
    private final List<String> myGenericIdentifiers;

    private List<JavaAnnotation> myAnnotations = new ArrayList<JavaAnnotation>();
    private Map<Integer, List<JavaAnnotation>> myParameterAnnotations = new HashMap<Integer, List<JavaAnnotation>>();

    private int myLastLocalVariableIndex;

    private final Frame myRootFrame = new Frame();
    private Frame myCurrentFrame = myRootFrame;

    private List<Statement> myBody = null;
    private List<Node> myNodes = null;

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

    public List<String> getImports() {
        return myImports;
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

    public void setLastLocalVariableIndex(int lastLocalVariableIndex) {
        this.myLastLocalVariableIndex = lastLocalVariableIndex;
    }

    public Frame getCurrentFrame() {
        return myCurrentFrame;
    }

    public void setCurrentFrame(final Frame currentFrame) {
        this.myCurrentFrame = currentFrame;
    }

    public JavaClassMethod(final String modifier, final String returnType, final String name, final String[] exceptions,
                           final JavaClass javaClass, final List<String> genericTypes, final List<String> genericIdentifiers,
                           final int textWidth, final int nestSize) {
        this.myModifier = modifier;
        this.myReturnType = returnType;
        this.myName = name;
        this.myExceptions = exceptions;
        this.myJavaClass = javaClass;
        this.myGenericTypes = genericTypes;
        this.myGenericIdentifiers = genericIdentifiers;
        this.myTextWidth = textWidth;
        this.myNestSize = nestSize;
    }

    public void addImport(final String importClassName) {
        myImports.add(importClassName);
    }

    public void addLocalVariableName(final int index, final String name) {
        myCurrentFrame.addLocalVariableName(index, name);
    }

    public void addLocalVariableType(final int index, final String type) {
        myCurrentFrame.addLocalVariableType(index, type);
    }

    public void addLocalVariableFromDebugInfo(final int index, final String name, final String type) {
        myRootFrame.addLocalVariableFromDebugInfo(index, name, type);
    }

    public List<String> getParameters() {
        List<String> parameters = new ArrayList<String>();
        for (int variableIndex = 1; variableIndex <= myLastLocalVariableIndex; variableIndex++) {
            if (myRootFrame.containsIndex(variableIndex)) {
                parameters.add(myRootFrame.getLocalVariableName(variableIndex));
            }
        }
        return parameters;
    }

    public boolean isGenericType(final String className) {
        return myGenericTypes.contains(className) || myJavaClass.isGenericType(className);
    }

    public String getGenericIdentifier(final String className) {
        if (!myGenericTypes.contains(className)) {
            return myJavaClass.getGenericIdentifier(className);
        } else {
            return myGenericIdentifiers.get(myGenericTypes.indexOf(className));
        }
    }

    public List<String> getGenericDeclaration() {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < myGenericTypes.size(); i++) {
            if (!myGenericTypes.get(i).equals("java/lang/Object")) {
                final String[] classParts = myGenericTypes.get(i).split("/");
                result.add(myGenericIdentifiers.get(i) + " extends " + classParts[classParts.length - 1]);
            } else {
                result.add(myGenericIdentifiers.get(i));
            }
        }
        return result;
    }

    public void appendAnnotation(final JavaAnnotation annotation) {
        myAnnotations.add(annotation);
    }

    public List<JavaAnnotation> getAnnotations() {
        return myAnnotations;
    }

    public void appendParameterAnnotation(final int index, final JavaAnnotation annotation) {
        if (!myParameterAnnotations.containsKey(index)) {
            myParameterAnnotations.put(index, new ArrayList<JavaAnnotation>());
        }
        myParameterAnnotations.get(index).add(annotation);
    }

    public boolean checkParameterForAnnotation(final int index) {
        return myParameterAnnotations.containsKey(index);
    }

    public List<JavaAnnotation> getParameterAnnotations(final int index) {
        return myParameterAnnotations.get(index);
    }

    public void setNodes(List<Node> myNodes) {
        this.myNodes = myNodes;
    }

    public void drawCFG() {
        GraphDrawer graphDrawer = new GraphDrawer(myNodes, myNestSize, myTextWidth);
        graphDrawer.draw();
        graphDrawer.simplyDraw();

    }

    @Override
    public String toString() {
          return PrettyPackage.pretty(myTextWidth, JavaClassPrinterPackage.printClassMethod(this));
    }
}
