package com.sdc.javascript;

import JavaClassPrinter.JavaClassPrinterPackage;
import com.sdc.abstractLangauge.AbstractClass;
import pretty.PrettyPackage;

import java.util.ArrayList;
import java.util.List;

public class JSClass extends AbstractClass{
    private final String myModifier;
    private final String myType;
    private final String myName;

    private final String mySuperClass;
    private final List<String> myImplementedInterfaces;

    private List<JSClassField> myFields = new ArrayList<JSClassField>();
    private List<JSClassMethod> myMethods = new ArrayList<JSClassMethod>();

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

    public List<String> getImplementedInterfaces() {
        return myImplementedInterfaces;
    }

    public String getSuperClass() {
        return mySuperClass;
    }

    public List<JSClassField> getFields() {
        return myFields;
    }

    public List<JSClassMethod> getMethods() {
        return myMethods;
    }

    public int getNestSize() {
        return myNestSize;
    }

    public JSClass(final String modifier, final String type, final String name,
                     final List<String> implementedInterfaces, final String superClass,
                     final int textWidth, final int nestSize) {
        this.myModifier = modifier;
        this.myType = type;
        this.myName = name;
        this.myImplementedInterfaces = implementedInterfaces;
        this.mySuperClass = superClass;
        this.myTextWidth = textWidth;
        this.myNestSize = nestSize;
    }

    public void appendField(JSClassField field) {
        myFields.add(field);
    }

    public void appendMethod(JSClassMethod method) {
        myMethods.add(method);
    }

    @Override
    public String toString() {
        //return PrettyPackage.pretty(myTextWidth, JavaClassPrinterPackage.printJavaClass(this));
        return JSClassPrinter.printJSClass(this);
    }
}
