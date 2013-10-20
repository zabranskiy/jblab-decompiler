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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    public Method(final @NotNull String modifier,
                  final @NotNull String returnType,
                  final @NotNull String name,
                  final @NotNull String signature,
                  final @Nullable String[] exceptions,
                  final @NotNull GeneralClass generalClass,
                  final @NotNull List<String> genericTypes,
                  final @NotNull List<String> genericIdentifiers,
                  final int textWidth,
                  final int nestSize) {
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

    @NotNull
    protected abstract String getInheritanceIdentifier();

    protected abstract int getParametersStartIndex();

    @NotNull
    public abstract Frame createFrame();

    @NotNull
    public String getModifier() {
        return myModifier;
    }

    @NotNull
    public String getReturnType() {
        return myReturnType;
    }

    @NotNull
    public String getName() {
        return myName;
    }

    @NotNull
    public String getSignature() {
        return mySignature;
    }

    public void setName(final @NotNull String name) {
        this.myName = name;
    }

    @Nullable
    public String[] getExceptions() {
        return myExceptions;
    }

    @NotNull
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

    @Nullable
    public MethodVisitorStub.DecompilerException getError() {
        return myError;
    }

    public void setError(final @NotNull MethodVisitorStub.DecompilerException error) {
        this.myError = error;
    }

    public void setLastLocalVariableIndex(final int lastLocalVariableIndex) {
        this.myLastLocalVariableIndex = lastLocalVariableIndex;
    }

    @NotNull
    public GeneralClass getDecompiledClass() {
        return myGeneralClass;
    }

    public boolean isNormalClassMethod() {
        return myGeneralClass.isNormalClass();
    }

    public void addImport(final @NotNull String importClassName) {
        myImports.add(importClassName);
    }

    @NotNull
    public Frame getCurrentFrame() {
        return myFrames.get(myFrames.size() - 1);
    }

    @NotNull
    public Frame getRootFrame() {
        return myFrames.get(0);
    }

    public void addNewFrame(final @NotNull Frame frame) {
        myFrames.add(frame);
    }

    public void addThisVariable(final @NotNull Type type) {
        getCurrentFrame().createAndInsertVariable(0, type, "this");
    }

    public void updateVariableInformation(final int index, final @NotNull Type type, final @Nullable Constant name) {
        getCurrentFrame().updateVariableInformation(index, type, name);
    }

    public void updateVariableInformationFromDebugInfo(final int index,
                                                       final @NotNull Type type,
                                                       final @NotNull Constant name,
                                                       final @NotNull Label start,
                                                       final @NotNull Label end) {
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

    public void updateVariableNameFromDebugInfo(final int index,
                                                final @NotNull Constant name,
                                                final @NotNull Label start,
                                                final @NotNull Label end) {
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

    @NotNull
    public List<Variable> getParameters() {
        return getRootFrame().getMethodParameters(getParametersStartIndex());
    }

    public boolean isGenericType(final @NotNull String className) {
        return myGenericTypes.contains(className) || myGeneralClass.isGenericType(className);
    }

    @Nullable
    public String getGenericIdentifier(final @NotNull String className) {
        if (!myGenericTypes.contains(className)) {
            return myGeneralClass.getGenericIdentifier(className);
        } else {
            return myGenericIdentifiers.get(myGenericTypes.indexOf(className));
        }
    }

    @NotNull
    public List<String> getGenericDeclaration() {
        final List<String> result = new ArrayList<String>();
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

    public void appendAnnotation(final @NotNull Annotation annotation) {
        myAnnotations.add(annotation);
    }

    @NotNull
    public List<Annotation> getAnnotations() {
        return myAnnotations;
    }

    public void appendParameterAnnotation(final int index, final @NotNull Annotation annotation) {
        if (!myParameterAnnotations.containsKey(index)) {
            myParameterAnnotations.put(index, new ArrayList<Annotation>());
        }
        myParameterAnnotations.get(index).add(annotation);
    }

    public boolean checkParameterForAnnotation(final int index) {
        return myParameterAnnotations.containsKey(index);
    }

    @Nullable
    public List<Annotation> getParameterAnnotations(final int index) {
        return myParameterAnnotations.get(index);
    }

    public void addInitializerToField(final @NotNull String fieldName, final @NotNull Expression initializer) {
        myGeneralClass.addInitializerToField(fieldName, initializer, this);
    }

    public boolean hasFieldInitializer(final @NotNull String fieldName) {
        return myGeneralClass.hasFieldInitializer(fieldName, this);
    }

    @Nullable
    public Construction getBegin() {
        return myBegin;
    }

    public void setBegin(final @NotNull Construction myBegin) {
        this.myBegin = myBegin;
    }

    public boolean hasEmptyBody() {
        if (myBegin instanceof ElementaryBlock) {
            final List<Statement> statements = ((ElementaryBlock) myBegin).getStatements();
            return statements.isEmpty()
                    || statements.size() == 1
                    && statements.get(0) instanceof Return
                    && ((Return) statements.get(0)).getReturnValue() == null;
        }
        return false;
    }

    public boolean isConstructor() {
        return myName.equals(myGeneralClass.getName());
    }

    @NotNull
    public String getNewTypeName(final @NotNull Type type) {
        final String variableType = type.toStringWithoutBrackets().trim().replace(".", "");

        String suffix = "";
        for (int i = 0; i < type.getDimensions(); i++) {
            suffix += "Arr";
        }

        final char firstChar = variableType.charAt(0);
        String name = Character.toLowerCase(firstChar) + ""; //primitive type
        Integer index;

        if (!type.isPrimitive()) {
            //for Classes
            final String prefix = charIsVowel(firstChar) ? "an" : "a";
            name = prefix + variableType;
        }

        name += suffix;

        index = myTypeNameIndices.get(name);
        if (index == null) {
            myTypeNameIndices.put(name, 1);
        } else {
            myTypeNameIndices.put(name, index + 1);
            name += index;
        }

        return name;
    }

    public static boolean charIsVowel(final char c) {
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
        final GraphDrawer graphDrawer = new GraphDrawer(myNodes, myNestSize, myTextWidth);
        graphDrawer.draw();
        graphDrawer.simplyDraw();
    }
*/
}
