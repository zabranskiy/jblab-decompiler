package com.sdc.languages.general.languageParts;

import com.sdc.ast.expressions.Expression;
import com.sdc.util.DeclarationWorker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

        public InnerClassIdentifier(final @NotNull String owner,
                                    final @Nullable String name,
                                    final @Nullable String descriptor) {
            this.myOwner = owner;
            this.myName = name;
            this.myDescriptor = descriptor;
        }

        @NotNull
        public String getOwner() {
            return myOwner;
        }

        @Nullable
        public String getName() {
            return myName;
        }

        @Nullable
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

    protected final List<ClassField> myFields = new ArrayList<ClassField>();
    protected final List<Method> myMethods = new ArrayList<Method>();

    protected final List<Annotation> myAnnotations = new ArrayList<Annotation>();

    protected final List<String> myImports = new ArrayList<String>();

    protected final List<String> myDefaultPackages = new ArrayList<String>();

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

    public GeneralClass(final @NotNull String modifier,
                        final @NotNull ClassType type,
                        final @NotNull String name,
                        final @NotNull String packageName,
                        final @NotNull List<String> implementedInterfaces,
                        final @NotNull String superClass,
                        final @NotNull List<String> genericTypes,
                        final @NotNull List<String> genericIdentifiers,
                        final int textWidth,
                        final int nestSize) {
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

    @NotNull
    public String getModifier() {
        return myModifier;
    }

    @NotNull
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

    @NotNull
    public ClassType getType() {
        return myType;
    }

    @NotNull
    public String getName() {
        return myName;
    }

    @NotNull
    public String getPackage() {
        return myPackage;
    }

    @NotNull
    public List<String> getImports() {
        return myImports;
    }

    @NotNull
    public List<String> getImplementedInterfaces() {
        return myImplementedInterfaces;
    }

    @NotNull
    public String getSuperClass() {
        return mySuperClass;
    }

    @NotNull
    public List<ClassField> getFields() {
        return myFields;
    }

    @NotNull
    public List<Method> getMethods() {
        return myMethods;
    }

    public int getNestSize() {
        return myNestSize;
    }

    public int getTextWidth() {
        return myTextWidth;
    }

    @Nullable
    public String getFullClassName() {
        return myFullClassName;
    }

    public void setFullClassName(final @NotNull String fullClassName) {
        this.myFullClassName = fullClassName;
    }

    public boolean isNormalClass() {
        return myIsNormalClass;
    }

    public void setIsNormalClass(final boolean isNormalClass) {
        this.myIsNormalClass = isNormalClass;
    }

    public boolean isLambdaFunctionClass() {
        return myIsLambdaFunctionClass;
    }

    public void setIsLambdaFunctionClass(final boolean isLambdaFunctionClass) {
        this.myIsLambdaFunctionClass = isLambdaFunctionClass;
    }

    public boolean isNestedClass() {
        return myIsNestedClass;
    }

    public void setIsNestedClass(final boolean isNestedClass) {
        this.myIsNestedClass = isNestedClass;
    }

    @Nullable
    public GeneralClass getOuterClass(final @Nullable String name) {
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

    public void setOuterClass(final @Nullable GeneralClass outerClass) {
        this.myOuterClass = outerClass;
    }

    public void appendField(final @NotNull ClassField field) {
        myFields.add(field);
    }

    public void appendMethod(final @NotNull Method method) {
        myMethods.add(method);
    }

    public void appendImports(final @NotNull List<String> imports) {
        for (final String importName : imports) {
            appendImport(importName);
        }
    }

    public void appendImport(final @NotNull String importName) {
        if (!hasImport(importName) && !checkImportNameForBeingInPackages(importName, myDefaultPackages)
                && !importName.contains("." + myName + ".")
                && !importName.contains("..")
                && !importName.equals(myName)) {
            myImports.add(importName);
        }
    }

    public void appendAnnotation(final @NotNull Annotation annotation) {
        myAnnotations.add(annotation);
    }

    @NotNull
    public List<Annotation> getAnnotations() {
        return myAnnotations;
    }

    public boolean isGenericType(final @NotNull String className) {
        return myGenericTypes.contains(className);
    }

    @Nullable
    public String getGenericIdentifier(final @NotNull String className) {
        return myGenericIdentifiers.get(myGenericTypes.indexOf(className));
    }

    public void addInitializerToField(final @NotNull String fieldName,
                                      final @NotNull Expression initializer,
                                      final @NotNull Method method) {
        final ClassField field = getField(fieldName);
        if (field != null) {
            field.setInitializer(initializer);
            field.addConstructor(method);
        }
    }

    @Nullable
    public ClassField getField(final @NotNull String fieldName) {
        for (final ClassField field : myFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    public boolean hasField(final @NotNull String fieldName) {
        for (final ClassField field : myFields) {
            if (field.getName().equalsIgnoreCase(fieldName)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasFieldInitializer(final @NotNull String fieldName, final @NotNull Method method) {
        final ClassField field = getField(fieldName);
        return field != null && field.getInitializer() != null && field.hasInitializer(method);
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

    public void addAnonymousClass(final @NotNull String className, final @NotNull GeneralClass decompiledClass) {
        myAnonymousClasses.put(className, decompiledClass);
    }

    public void addInnerClass(final @NotNull String innerClassName, final @NotNull GeneralClass decompiledClass) {
        myInnerClasses.put(innerClassName, decompiledClass);
    }

    public InnerClassIdentifier getInnerClassIdentifier() {
        return myInnerClassIdentifier;
    }

    public void setInnerClassIdentifier(final @NotNull String owner,
                                        final @Nullable String name,
                                        final @Nullable String descriptor) {
        myInnerClassIdentifier = new InnerClassIdentifier(owner, name, descriptor);
    }

    @NotNull
    public List<GeneralClass> getMethodInnerClasses(final @NotNull String methodName, final @Nullable String descriptor) {
        final List<GeneralClass> result = new ArrayList<GeneralClass>();
        for (final Map.Entry<String, GeneralClass> innerClass : myInnerClasses.entrySet()) {
            final String name = innerClass.getValue().getInnerClassIdentifier().getName();
            final String desc = innerClass.getValue().getInnerClassIdentifier().getDescriptor();

            if (name != null && name.equals(methodName)
                    && (desc == null && descriptor == null || desc != null && desc.equals(descriptor))) {
                result.add(innerClass.getValue());
            }
        }
        return result;
    }

    @NotNull
    public List<GeneralClass> getClassBodyInnerClasses() {
        final List<GeneralClass> result = new ArrayList<GeneralClass>();
        for (final Map.Entry<String, GeneralClass> innerClass : myInnerClasses.entrySet()) {
            final InnerClassIdentifier innerClassIdentifier = innerClass.getValue().getInnerClassIdentifier();
            if (innerClassIdentifier.getName() == null && innerClassIdentifier.getOwner().equals(myName)) {
                result.add(innerClass.getValue());
            }
        }
        return result;
    }

    public boolean hasAnonymousClass(final @NotNull String name) {
        return myAnonymousClasses.containsKey(name);
    }

    @Nullable
    public GeneralClass getAnonymousClass(final @NotNull String name) {
        return myAnonymousClasses.get(name);
    }

    public void addInnerClassError(final @NotNull String className, final @NotNull Exception exception) {
        myInnerClassesErrors.put(className, exception);
    }

    @NotNull
    public Map<String, Exception> getInnerClassesErrors() {
        return myInnerClassesErrors;
    }

    @NotNull
    public String decompileClassNameWithOuterClasses(final @NotNull String fullClassName) {
        final String className = DeclarationWorker.decompileClassNameWithOuterClasses(fullClassName);

        return removeClassPrefix(className);
    }

    @NotNull
    public String getDescriptor(final @NotNull String descriptor,
                                final int pos,
                                final @NotNull List<String> imports,
                                final @NotNull DeclarationWorker.SupportedLanguage language) {
        final String decompiledDescriptor = DeclarationWorker.getDescriptor(descriptor, pos, imports, language);

        return removeClassPrefix(decompiledDescriptor);
    }

    protected boolean checkImportNameForBeingInPackage(final @NotNull String importName,
                                                       final @NotNull String packageName) {
        return importName.indexOf(packageName) == 0 && importName.lastIndexOf(".") == packageName.length();
    }

    protected boolean checkImportNameForBeingInPackages(final @NotNull String importName,
                                                        final @NotNull List<String> packageNames) {
        boolean result = false;
        for (final String packageName : packageNames) {
            result |= checkImportNameForBeingInPackage(importName, packageName);
        }
        return result;
    }

    protected boolean hasImport(final @NotNull String importName) {
        return myImports.contains(importName);
    }

    @NotNull
    private String removeClassPrefix(final @NotNull String className) {
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
