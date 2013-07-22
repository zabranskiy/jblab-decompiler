package com.sdc.java;

import com.sdc.abstractLanguage.AbstractClassVisitor;
import com.sdc.util.DeclarationWorker;
import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.List;

public class JavaClassVisitor extends AbstractClassVisitor {
    private JavaClass myDecompiledJavaClass = null;
    private final int myTextWidth;
    private final int myNestSize;

    public JavaClassVisitor(final int textWidth, final int nestSize) {
        this.myTextWidth = textWidth;
        this.myNestSize = nestSize;
    }

    @Override
    public void visit(final int version, final int access, final String name
            , final String signature, final String superName, final String[] interfaces) {
        final String modifier = DeclarationWorker.getJavaAccess(access & ~Opcodes.ACC_SUPER);
        String type = "";

        if ((access & Opcodes.ACC_ENUM) == 0
                && (access & Opcodes.ACC_INTERFACE) == 0
                && (access & Opcodes.ACC_ANNOTATION) == 0) {
            type = "class ";
        }

        final String className = DeclarationWorker.getClassName(name);

        final String[] classParts = name.split("/");
        StringBuilder packageName = new StringBuilder("");
        for (int i = 0; i < classParts.length - 2; i++) {
            packageName.append(classParts[i]).append(".");
        }
        packageName.append(classParts[classParts.length - 2]);

        String superClass = "";
        String superClassImport = "";
        if (superName != null && !"java/lang/Object".equals(superName)) {
            superClass = DeclarationWorker.getClassName(superName);
            superClassImport = DeclarationWorker.getDecompiledFullClassName(superName);
        }

        List<String> implementedInterfaces = new ArrayList<String>();
        List<String> implementedInterfacesImports = new ArrayList<String>();
        if (interfaces != null && interfaces.length > 0) {
            for (final String implInterface : interfaces) {
                implementedInterfaces.add(DeclarationWorker.getClassName(implInterface));
                implementedInterfacesImports.add(DeclarationWorker.getDecompiledFullClassName(implInterface));
            }
        }

        List<String> genericTypesList = new ArrayList<String>();
        List<String> genericIdentifiersList = new ArrayList<String>();
        List<String> genericTypesImports = new ArrayList<String>();
        DeclarationWorker.parseGenericDeclaration(signature, genericTypesList, genericIdentifiersList, genericTypesImports);

        myDecompiledJavaClass = new JavaClass(modifier, type, className, packageName.toString(), implementedInterfaces
                , superClass, genericTypesList, genericIdentifiersList, myTextWidth, myNestSize);

        if (!superClassImport.isEmpty()) {
            myDecompiledJavaClass.appendImport(superClassImport);
        }

        myDecompiledJavaClass.appendImports(implementedInterfacesImports);
        myDecompiledJavaClass.appendImports(genericTypesImports);
    }

    @Override
    public void visitSource(final String source, final String debug) {
    }

    @Override
    public void visitOuterClass(final String owner, final String name, final String desc) {
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        JavaAnnotation annotation = new JavaAnnotation();
        List<String> annotationImports = new ArrayList<String>();

        annotation.setName(DeclarationWorker.getJavaDescriptor(desc, 0, annotationImports));
        myDecompiledJavaClass.appendAnnotation(annotation);
        myDecompiledJavaClass.appendImports(annotationImports);

        return new JavaAnnotationVisitor(annotation);
    }

    @Override
    public void visitAttribute(final Attribute attr) {
    }

    @Override
    public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
        final String description = signature != null ? signature : desc;
        List<String> fieldDeclarationImports = new ArrayList<String>();

        final JavaClassField cf = new JavaClassField(DeclarationWorker.getJavaAccess(access)
                , DeclarationWorker.getJavaDescriptor(description, 0, fieldDeclarationImports)
                , name, myTextWidth, myNestSize);
        myDecompiledJavaClass.appendField(cf);
        myDecompiledJavaClass.appendImports(fieldDeclarationImports);

        return null;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc
            , final String signature, final String[] exceptions) {

        final String description = signature != null ? signature : desc;
        final String modifier = DeclarationWorker.getJavaAccess(access);

        List<String> throwedExceptions = new ArrayList<String>();
        if (exceptions != null) {
            for (final String exception : exceptions) {
                throwedExceptions.add(DeclarationWorker.getClassName(exception));
            }
        }

        List<String> genericTypesList = new ArrayList<String>();
        List<String> genericIdentifiersList = new ArrayList<String>();
        List<String> genericTypesImports = new ArrayList<String>();
        DeclarationWorker. parseGenericDeclaration(description, genericTypesList, genericIdentifiersList, genericTypesImports);

        String returnType;
        String methodName;
        if (name.equals("<init>")) {
            returnType = "";
            methodName = myDecompiledJavaClass.getName();
        } else {
            List<String> methodReturnTypeImports = new ArrayList<String>();
            final int returnTypeIndex = description.indexOf(')') + 1;
            returnType = DeclarationWorker.getJavaDescriptor(description, returnTypeIndex, methodReturnTypeImports);
            methodName = name;
            myDecompiledJavaClass.appendImports(methodReturnTypeImports);
        }

        final JavaMethod javaMethod = new JavaMethod(modifier, returnType, methodName
                , throwedExceptions.toArray(new String[throwedExceptions.size()])
                , myDecompiledJavaClass, genericTypesList, genericIdentifiersList
                , myTextWidth, myNestSize);

        myDecompiledJavaClass.appendImports(genericTypesImports);

        final String parameters = description.substring(description.indexOf('(') + 1, description.indexOf(')'));
        DeclarationWorker.addInformationAboutParameters(parameters, javaMethod, 1, DeclarationWorker.SupportedLanguage.JAVA);

        myDecompiledJavaClass.appendMethod(javaMethod);

        return new JavaMethodVisitor(javaMethod
                , myDecompiledJavaClass.getPackage() + "." + myDecompiledJavaClass.getName());
    }

    @Override
    public void visitEnd() {
        for (final JavaMethod method : myDecompiledJavaClass.getMethods()) {
            myDecompiledJavaClass.appendImports(method.getImports());
        }
    }

    public String getDecompiledCode() {
        return myDecompiledJavaClass.toString();
    }
}