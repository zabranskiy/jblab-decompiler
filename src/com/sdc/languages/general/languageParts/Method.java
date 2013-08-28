package com.sdc.languages.general.languageParts;

import com.sdc.ast.Type;
import com.sdc.ast.controlflow.Return;
import com.sdc.ast.controlflow.Statement;
import com.sdc.ast.expressions.Constant;
import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.identifiers.Variable;
import com.sdc.cfg.constructions.Construction;
import com.sdc.cfg.constructions.ElementaryBlock;
import com.sdc.languages.general.astUtils.Frame;
import com.sdc.languages.general.visitors.MethodVisitorStub;
import org.objectweb.asm.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Method {
    protected final String myModifier;
    protected final String myReturnType;
    protected String myName;
    protected final String mySignature;
    protected final String[] myExceptions;

    protected List<String> myImports = new ArrayList<String>();

    protected final GeneralClass myGeneralClass;
    protected final List<String> myGenericTypes;
    protected final List<String> myGenericIdentifiers;

    protected List<Annotation> myAnnotations = new ArrayList<Annotation>();
    protected Map<Integer, List<Annotation>> myParameterAnnotations = new HashMap<Integer, List<Annotation>>();
    protected Map<String, Integer> myTypeNameIndices = new HashMap<String, Integer>();

    protected List<Frame> myFrames = new ArrayList<Frame>();

    protected int myLastLocalVariableIndex;

    protected MethodVisitorStub.DecompilerException myError = null;

    protected Construction myBegin;

    protected final int myTextWidth;
    protected final int myNestSize;

    public Method(final String modifier, final String returnType, final String name, final String signature, final String[] exceptions,
                  final GeneralClass generalClass, final List<String> genericTypes, final List<String> genericIdentifiers,
                  final int textWidth, final int nestSize) {
        this.myModifier = modifier;
        this.myReturnType = returnType;
        this.myName = name;
        this.mySignature = signature;
        this.myExceptions = exceptions;
        this.myGeneralClass = generalClass;
        this.myGenericTypes = genericTypes;
        this.myGenericIdentifiers = genericIdentifiers;
        this.myTextWidth = textWidth;
        this.myNestSize = nestSize;

        this.myFrames.add(createFrame());
    }

    protected abstract String getInheritanceIdentifier();

    protected abstract int getParametersStartIndex();

    public abstract Frame createFrame();

    public String getModifier() {
        return myModifier;
    }

    public String getReturnType() {
        return myReturnType;
    }

    public String getName() {
        return myName;
    }

    public String getSignature() {
        return mySignature;
    }

    public void setName(final String name) {
        this.myName = name;
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

    public int getTextWidth() {
        return myTextWidth;
    }

    public MethodVisitorStub.DecompilerException getError() {
        return myError;
    }

    public void setError(final MethodVisitorStub.DecompilerException error) {
        this.myError = error;
    }

    public void setLastLocalVariableIndex(int lastLocalVariableIndex) {
        this.myLastLocalVariableIndex = lastLocalVariableIndex;
    }

    public GeneralClass getDecompiledClass() {
        return myGeneralClass;
    }

    public boolean isNormalClassMethod() {
        return myGeneralClass.isNormalClass();
    }

    public void addImport(final String importClassName) {
        myImports.add(importClassName);
    }

    public Frame getCurrentFrame() {
        if (!myFrames.isEmpty()) {
            return myFrames.get(myFrames.size() - 1);
        }
        return null;
    }

    public Frame getRootFrame() {
        if (!myFrames.isEmpty()) {
            return myFrames.get(0);
        }
        return null;
    }

    public void addNewFrame(final Frame frame) {
        myFrames.add(frame);
    }

    public void addThisVariable(final Type type) {
        getCurrentFrame().createAndInsertVariable(0, type, "this");
    }

    public void updateVariableInformation(final int index, final Type type, final Constant name) {
        getCurrentFrame().updateVariableInformation(index, type, name);
    }

    public void updateVariableInformationFromDebugInfo(final int index, final Type type, final Constant name, final Label start, final Label end) {
        boolean started = false;

        for (final Frame frame : myFrames) {
            if (started || frame.hasLabel(start)) {
                if (!started) {
                    frame.getVariable(index).cutParent();
                }
                frame.updateVariableInformation(index, type, name);

                started = true;
                if (frame.hasLabel(end)) {
                    return;
                }
            }
        }
    }

    public void updateVariableNameFromDebugInfo(final int index, final Constant name, final Label start, final Label end) {
        for (final Frame frame : myFrames) {
            if (frame.hasLabel(start)) {
                Variable variable = frame.getVariable(index);
                updateVariableInformationFromDebugInfo(index, variable.getType(), name, start, end);
            }
        }
    }

    public void declareThisVariable() {
        getCurrentFrame().getVariable(0).declare();
    }

    public List<Variable> getParameters() {
        return getRootFrame().getMethodParameters(getParametersStartIndex());
    }

    public boolean isGenericType(final String className) {
        return myGenericTypes.contains(className) || myGeneralClass.isGenericType(className);
    }

    public String getGenericIdentifier(final String className) {
        if (!myGenericTypes.contains(className)) {
            return myGeneralClass.getGenericIdentifier(className);
        } else {
            return myGenericIdentifiers.get(myGenericTypes.indexOf(className));
        }
    }

    public List<String> getGenericDeclaration() {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < myGenericTypes.size(); i++) {
            final String genericType = myGenericTypes.get(i);
            if (!genericType.equals("Object ") && !genericType.equals("Any?")) {
                result.add(myGenericIdentifiers.get(i) + " " + getInheritanceIdentifier() + " " + genericType.trim());
            } else {
                result.add(myGenericIdentifiers.get(i));
            }
        }
        return result;
    }

    public void appendAnnotation(final Annotation annotation) {
        myAnnotations.add(annotation);
    }

    public List<Annotation> getAnnotations() {
        return myAnnotations;
    }

    public void appendParameterAnnotation(final int index, final Annotation annotation) {
        if (!myParameterAnnotations.containsKey(index)) {
            myParameterAnnotations.put(index, new ArrayList<Annotation>());
        }
        myParameterAnnotations.get(index).add(annotation);
    }

    public boolean checkParameterForAnnotation(final int index) {
        return myParameterAnnotations.containsKey(index);
    }

    public List<Annotation> getParameterAnnotations(final int index) {
        return myParameterAnnotations.get(index);
    }

    public void addInitializerToField(final String fieldName, final Expression initializer) {
        myGeneralClass.addInitializerToField(fieldName, initializer);
    }

    public boolean hasFieldInitializer(final String fieldName) {
        return myGeneralClass.hasFieldInitializer(fieldName);
    }

    public void setBegin(Construction myBegin) {
        this.myBegin = myBegin;
    }

    public Construction getBegin() {
        return myBegin;
    }

    public boolean hasEmptyBody() {
        if (myBegin instanceof ElementaryBlock) {
            final List<Statement> statements = ((ElementaryBlock) myBegin).getStatements();
            return statements.isEmpty() || statements.size() == 1 && statements.get(0) instanceof Return && ((Return) statements.get(0)).getReturnValue() == null;
        }
        return false;
    }

    public String getNewTypeName(Type type) {
        String variableType = type.toStringWithoutBrackets().trim().replace(".", "");
        String suffix="";
        for (int i = 0; i < type.getDimensions(); i++) {
            suffix+="Arr";
        }
        char firstChar = variableType.charAt(0);
        String name = Character.toLowerCase(firstChar) + ""; //primitive type
        Integer index;
        if (!type.isPrimitive()) {
            //for Classes
            String prefix = charIsVowel(firstChar) ? "an" : "a";
            name = prefix + variableType;
        }
        name+=suffix;
        index = myTypeNameIndices.get(name);
        if (index == null) {
            myTypeNameIndices.put(name, 1);
        } else {
            myTypeNameIndices.put(name, index + 1);
            name += index;
        }
        return name;
    }

    public static boolean charIsVowel(char c) {
        switch (Character.toLowerCase(c)) {
            case 'a':
            case 'e':
            case 'u':
            case 'y':
            case 'o':
            case 'i':
                return true;
            default:
                return false;
        }
    }
/*
    public void drawCFG() {
        GraphDrawer graphDrawer = new GraphDrawer(myNodes, myNestSize, myTextWidth);
        graphDrawer.draw();
        graphDrawer.simplyDraw();
    }
*/
}
