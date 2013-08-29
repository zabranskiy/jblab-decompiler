package com.sdc.languages.general.languageParts;

import com.sdc.ast.expressions.Expression;
import com.sdc.util.DeclarationWorker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneralClass {
    public enum ClassType {
        INTERFACE, ANNOTATION, ENUM, SIMPLE_CLASS, ABSTRACT_CLASS
    }

    public class InnerClassIdentifier {
        private final String myOwner;
        private final String myName;
        private final String myDescriptor;

        public InnerClassIdentifier(final String owner, final String name, final String descriptor) {
            this.myOwner = owner;
            this.myName = name;
            this.myDescriptor = descriptor;
        }

        public String getOwner() {
            return myOwner;
        }

        public String getName() {
            return myName;
        }

        public String getDescriptor() {
            return myDescriptor;
        }
    }

    protected final String myModifier;
    protected final ClassType myType;
    protected final String myName;
    protected String myFullClassName;
    protected final String myPackage;

    protected final String mySuperClass;
    protected final List<String> myImplementedInterfaces;

    protected final List<String> myGenericTypes;
    protected final List<String> myGenericIdentifiers;

    protected List<ClassField> myFields = new ArrayList<ClassField>();
    protected List<Method> myMethods = new ArrayList<Method>();

    protected List<Annotation> myAnnotations = new ArrayList<Annotation>();

    protected List<String> myImports = new ArrayList<String>();

    protected List<String> myDefaultPackages = new ArrayList<String>();

    protected boolean myIsNormalClass = true;
    protected boolean myIsLambdaFunctionClass = false;
    protected boolean myIsNestedClass = false;
    protected boolean myIsEnum = false;
    protected boolean myIsInterface = false;
    protected boolean myIsAbstractClass = false;
    protected boolean myIsAnnotation = false;

    protected Map<String, GeneralClass> myAnonymousClasses = new HashMap<String, GeneralClass>();
    protected Map<String, GeneralClass> myInnerClasses = new HashMap<String, GeneralClass>();
    protected InnerClassIdentifier myInnerClassIdentifier;

    protected GeneralClass myOuterClass;

    protected Map<String, Exception> myInnerClassesErrors = new HashMap<String, Exception>();

    protected final int myTextWidth;
    protected final int myNestSize;

    public GeneralClass(final String modifier, final ClassType type, final String name, final String packageName,
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

    protected abstract String getInheritanceIdentifier();

    public String getModifier() {
        return myModifier;
    }

    public String getTypeToString() {
        switch (myType) {
            case ENUM:
                return "enum ";
            case INTERFACE:
                return "interface ";
            case ANNOTATION:
                return "annotation ";
            default:
                return "class ";
        }
    }

    public ClassType getType() {
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

    public List<ClassField> getFields() {
        return myFields;
    }

    public List<Method> getMethods() {
        return myMethods;
    }

    public int getNestSize() {
        return myNestSize;
    }

    public int getTextWidth() {
        return myTextWidth;
    }

    public void setFullClassName(final String fullClassName) {
        this.myFullClassName = fullClassName;
    }

    public String getFullClassName() {
        return myFullClassName;
    }

    public void setIsNormalClass(final boolean isNormalClass) {
        this.myIsNormalClass = isNormalClass;
    }

    public boolean isNormalClass() {
        return myIsNormalClass;
    }

    public void setIsLambdaFunctionClass(final boolean isLambdaFunctionClass) {
        this.myIsLambdaFunctionClass = isLambdaFunctionClass;
    }

    public void setIsNestedClass(final boolean isNestedClass) {
        this.myIsNestedClass = isNestedClass;
    }

    public boolean isNestedClass() {
        return myIsNestedClass;
    }

    public boolean isLambdaFunctionClass() {
        return myIsLambdaFunctionClass;
    }

    public GeneralClass getOuterClass(final String name) {
        if (myName.equals(name) || name == null) {
            return this;
        }

        if (myOuterClass != null) {
            if (myOuterClass.getName().equals(name)) {
                return myOuterClass;
            } else {
                myOuterClass.getOuterClass(name);
            }
        }
        return null;
    }

    public void setOuterClass(final GeneralClass outerClass) {
        this.myOuterClass = outerClass;
    }

    public void appendField(final ClassField field) {
        myFields.add(field);
    }

    public void appendMethod(final Method method) {
        myMethods.add(method);
    }

    public void appendImports(final List<String> imports) {
        for (final String importName : imports) {
            appendImport(importName);
        }
    }

    public void appendImport(final String importName) {
        if (!hasImport(importName) && !checkImportNameForBeingInPackages(importName, myDefaultPackages)
                && !importName.contains("." + myName + ".") && !importName.contains("..") && !importName.equals(myName))
        {
            myImports.add(importName);
        }
    }

    public void appendAnnotation(final Annotation annotation) {
        myAnnotations.add(annotation);
    }

    public List<Annotation> getAnnotations() {
        return myAnnotations;
    }

    public boolean isGenericType(final String className) {
        return myGenericTypes.contains(className);
    }

    public String getGenericIdentifier(final String className) {
        return myGenericIdentifiers.get(myGenericTypes.indexOf(className));
    }

    public void addInitializerToField(final String fieldName, final Expression initializer) {
        getField(fieldName).setInitializer(initializer);
    }

    public ClassField getField(final String fieldName) {
        for (ClassField field : myFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    public boolean hasField(final String fieldName) {
        for (ClassField field : myFields) {
            if (field.getName().equalsIgnoreCase(fieldName)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasFieldInitializer(final String fieldName) {
        return getField(fieldName).getInitializer() != null;
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

    public void addAnonymousClass(final String className, final GeneralClass decompiledClass) {
        myAnonymousClasses.put(className, decompiledClass);
    }

    public void addInnerClass(final String innerClassName, final GeneralClass decompiledClass) {
        myInnerClasses.put(innerClassName, decompiledClass);
    }

    public void setInnerClassIdentifier(final String owner, final String name, final String descriptor) {
        myInnerClassIdentifier = new InnerClassIdentifier(owner, name, descriptor);
    }

    public InnerClassIdentifier getInnerClassIdentifier() {
        return myInnerClassIdentifier;
    }

    public List<GeneralClass> getMethodInnerClasses(final String methodName, final String descriptor) {
        List<GeneralClass> result = new ArrayList<GeneralClass>();
        for (final Map.Entry<String, GeneralClass> innerClass : myInnerClasses.entrySet()) {
            final String name = innerClass.getValue().getInnerClassIdentifier().getName();
            final String desc = innerClass.getValue().getInnerClassIdentifier().getDescriptor();

            if (name != null && name.equals(methodName) && desc.equals(descriptor)) {
                result.add(innerClass.getValue());
            }
        }
        return result;
    }

    public List<GeneralClass> getClassBodyInnerClasses() {
        List<GeneralClass> result = new ArrayList<GeneralClass>();
        for (final Map.Entry<String, GeneralClass> innerClass : myInnerClasses.entrySet()) {
            final InnerClassIdentifier innerClassIdentifier = innerClass.getValue().getInnerClassIdentifier();
            if (innerClassIdentifier.getName() == null && innerClassIdentifier.getOwner().equals(myName)) {
                result.add(innerClass.getValue());
            }
        }
        return result;
    }

    public boolean hasAnonymousClass(final String name) {
        return myAnonymousClasses.containsKey(name);
    }

    public GeneralClass getAnonymousClass(final String name) {
        return myAnonymousClasses.get(name);
    }

    public void addInnerClassError(final String className, final Exception exception) {
        myInnerClassesErrors.put(className, exception);
    }

    public Map<String, Exception> getInnerClassesErrors() {
        return myInnerClassesErrors;
    }

    protected boolean checkImportNameForBeingInPackage(final String importName, final String packageName) {
        return importName.indexOf(packageName) == 0 && importName.lastIndexOf(".") == packageName.length();
    }

    protected boolean checkImportNameForBeingInPackages(final String importName, final List<String> packageNames) {
        boolean result = false;
        for (final String packageName : packageNames) {
            result |= checkImportNameForBeingInPackage(importName, packageName);
        }
        return result;
    }

    protected boolean hasImport(final String importName) {
        return myImports.contains(importName);
    }

    public String decompileClassNameWithOuterClasses(final String fullClassName) {
        final String className = DeclarationWorker.decompileClassNameWithOuterClasses(fullClassName);

        return removeClassPrefix(className);
    }

    public String getDescriptor(final String descriptor, final int pos, List<String> imports
            , final DeclarationWorker.SupportedLanguage language) {
        final String decompiledDescriptor = DeclarationWorker.getDescriptor(descriptor, pos, imports, language);

        return removeClassPrefix(decompiledDescriptor);
    }

    private String removeClassPrefix(final String className) {
        final String pattern1 = myName + ".";
        final String pattern2 = "." + pattern1;

        int startIndex = 0;
        if (className.startsWith(pattern1)) {
            startIndex = pattern1.length();
        } else if (className.contains(pattern2)) {
            startIndex = className.indexOf(pattern2) + pattern2.length();
        }

        final int anonymousClassIndex = className.indexOf("AnonymousClass__");
        if (anonymousClassIndex > -1) {
            return className.substring(anonymousClassIndex);
        }

        return className.substring(startIndex);
    }
}
