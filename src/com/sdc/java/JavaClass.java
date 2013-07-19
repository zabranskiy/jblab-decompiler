package com.sdc.java;

import com.sdc.abstractLanguage.AbstractClass;

import pretty.PrettyPackage;
import JavaPrinter.JavaPrinterPackage;

import java.util.ArrayList;
import java.util.List;

public class JavaClass extends AbstractClass {
    private final String myModifier;
    private final String myType;
    private final String myName;
    private final String myPackage;

    private final String mySuperClass;
    private final List<String> myImplementedInterfaces;

    private final List<String> myGenericTypes;
    private final List<String> myGenericIdentifiers;

    private List<JavaClassField> myFields = new ArrayList<JavaClassField>();
    private List<JavaMethod> myMethods = new ArrayList<JavaMethod>();

    private List<JavaAnnotation> myAnnotations = new ArrayList<JavaAnnotation>();

    private List<String> myImports = new ArrayList<String>();

    private final int myTextWidth;
    private final int myNestSize;

    public String getModifier() {
        return myModifier;
    }

    public String getType() {
        return myType;
    }

    public String getName() {
        return myName;
    }

    public String getPackage() {
        return myPackage;
    }

    public List<String> getImports() {
        return myImports;
    }

    public List<String> getImplementedInterfaces() {
        return myImplementedInterfaces;
    }

    public String getSuperClass() {
        return mySuperClass;
    }

    public List<JavaClassField> getFields() {
        return myFields;
    }

    public List<JavaMethod> getMethods() {
        return myMethods;
    }

    public int getNestSize() {
        return myNestSize;
    }

    public JavaClass(final String modifier, final String type, final String name, final String packageName,
                     final List<String> implementedInterfaces, final String superClass,
                     final List<String> genericTypes, final List<String> genericIdentifiers,
                     final int textWidth, final int nestSize) {
        this.myModifier = modifier;
        this.myType = type;
        this.myName = name;
        this.myPackage = packageName;
        this.myImplementedInterfaces = implementedInterfaces;
        this.mySuperClass = superClass;
        this.myGenericTypes = genericTypes;
        this.myGenericIdentifiers = genericIdentifiers;
        this.myTextWidth = textWidth;
        this.myNestSize = nestSize;
    }

    public void appendField(final JavaClassField field) {
        myFields.add(field);
    }

    public void appendMethod(final JavaMethod method) {
        myMethods.add(method);
    }

    public void appendImports(final List<String> imports) {
        for (final String importName : imports) {
            appendImport(importName);
        }
    }

    public void appendImport(final String importName) {
        if (!myImports.contains(importName)
                && (importName.indexOf(myPackage) != 0 || importName.lastIndexOf(".") != myPackage.length()))
        {
            myImports.add(importName);
        }
    }

    public void appendAnnotation(final JavaAnnotation annotation) {
        myAnnotations.add(annotation);
    }

    public List<JavaAnnotation> getAnnotations() {
        return myAnnotations;
    }

    public boolean isGenericType(final String className) {
        return myGenericTypes.contains(className);
    }

    public String getGenericIdentifier(final String className) {
        return myGenericIdentifiers.get(myGenericTypes.indexOf(className));
    }

    public List<String> getGenericDeclaration() {
        List<String> result = new ArrayList<String>();
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

    @Override
    public String toString() {
        return PrettyPackage.pretty(myTextWidth, JavaPrinterPackage.printJavaClass(this));
    }
}
