package com.sdc.kotlin;

import com.sdc.abstractLanguage.AbstractClassVisitor;
import com.sdc.java.*;
import com.sdc.util.DeclarationWorker;
import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.List;

public class KotlinClassVisitor extends AbstractClassVisitor {
    private KotlinClass myDecompiledKotlinClass = null;
    private final int myTextWidth;
    private final int myNestSize;

    public KotlinClassVisitor(final int textWidth, final int nestSize) {
        this.myTextWidth = textWidth;
        this.myNestSize = nestSize;
    }

    @Override
    public void visit(final int version, final int access, final String name
            , final String signature, final String superName, final String[] interfaces) {

        final String modifier = DeclarationWorker.getKotlinAccess(access & ~Opcodes.ACC_SUPER);
        String type = "";

        if ((access & Opcodes.ACC_ENUM) == 0
                && (access & Opcodes.ACC_INTERFACE) == 0
                && (access & Opcodes.ACC_ANNOTATION) == 0)
        {
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

        List<String> traits = new ArrayList<String>();
        List<String> traitImports = new ArrayList<String>();
        if (interfaces != null && interfaces.length > 0) {
            for (final String implInterface : interfaces) {
                if (!implInterface.equals("jet/JetObject")) {
                    traits.add(DeclarationWorker.getClassName(implInterface));
                    traitImports.add(DeclarationWorker.getDecompiledFullClassName(implInterface));
                }
            }
        }

        List<String> genericTypesList = new ArrayList<String>();
        List<String> genericIdentifiersList = new ArrayList<String>();
        List<String> genericTypesImports = new ArrayList<String>();
        DeclarationWorker.parseGenericDeclaration(signature, genericTypesList, genericIdentifiersList, genericTypesImports);

        myDecompiledKotlinClass = new KotlinClass(modifier, type, className, packageName.toString(), traits
                , superClass, genericTypesList, genericIdentifiersList, myTextWidth, myNestSize);

        if (!superClassImport.isEmpty()) {
            myDecompiledKotlinClass.appendImport(superClassImport);
        }

        myDecompiledKotlinClass.appendImports(traitImports);
        myDecompiledKotlinClass.appendImports(genericTypesImports);

        if (className.contains("$src$")) {
            myDecompiledKotlinClass.setIsNormalClass(false);
        }
    }

    @Override
    public void visitSource(final String source, final String debug) {
    }

    @Override
    public void visitOuterClass(final String owner, final String name, final String desc) {
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        List<String> annotationImports = new ArrayList<String>();
        final String annotationName = DeclarationWorker.getKotlinDescriptor(desc, 0, annotationImports);
        if (!annotationName.startsWith("Jet")) {
            KotlinAnnotation annotation = new KotlinAnnotation();
            annotation.setName(annotationName);
            myDecompiledKotlinClass.appendAnnotation(annotation);
            myDecompiledKotlinClass.appendImports(annotationImports);
            return new KotlinAnnotationVisitor(annotation);
        } else {
            return null;
        }
    }

    @Override
    public void visitAttribute(final Attribute attr) {
    }

    @Override
    public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
        List<String> fieldImports = new ArrayList<String>();
        final String description = signature != null ? signature : desc;
        final KotlinClassField cf = new KotlinClassField(DeclarationWorker.getKotlinAccess(access)
                , DeclarationWorker.getKotlinDescriptor(description, 0, fieldImports)
                , name, myTextWidth, myNestSize);
        myDecompiledKotlinClass.appendField(cf);
        myDecompiledKotlinClass.appendImports(fieldImports);
        return null;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc
            , final String signature, final String[] exceptions) {

        final String description = signature != null ? signature : desc;
        final String modifier = DeclarationWorker.getKotlinAccess(access);

        List<String> genericTypesList = new ArrayList<String>();
        List<String> genericIdentifiersList = new ArrayList<String>();
        List<String> genericTypesImports = new ArrayList<String>();
        DeclarationWorker.parseGenericDeclaration(description, genericTypesList, genericIdentifiersList, genericTypesImports);

        String returnType;
        String methodName;
        if (name.equals("<init>")) {
            returnType = "";
            methodName = myDecompiledKotlinClass.getName();
        } else {
            List<String> methodReturnTypeImports = new ArrayList<String>();
            final int returnTypeIndex = description.indexOf(')') + 1;
            returnType = DeclarationWorker.getKotlinDescriptor(description, returnTypeIndex, methodReturnTypeImports);
            methodName = name;
            myDecompiledKotlinClass.appendImports(methodReturnTypeImports);
        }

        if ((methodName.startsWith("set") || methodName.startsWith("get")) && myDecompiledKotlinClass.hasField(methodName.substring(3))) {
            return null;
        }

        final KotlinMethod kotlinMethod = new KotlinMethod(modifier, returnType, methodName
                , myDecompiledKotlinClass, genericTypesList, genericIdentifiersList
                , myTextWidth, myNestSize);

        myDecompiledKotlinClass.appendImports(genericTypesImports);

        final String parameters = description.substring(description.indexOf('(') + 1, description.indexOf(')'));
        int startIndex = 1;
        if (!myDecompiledKotlinClass.isNormalClass()) {
            startIndex = 0;
        }

        DeclarationWorker.addInformationAboutParameters(parameters, kotlinMethod, startIndex, DeclarationWorker.SupportedLanguage.KOTLIN);

        if (name.equals("<init>")) {
            myDecompiledKotlinClass.setConstructor(kotlinMethod);
        } else {
            myDecompiledKotlinClass.appendMethod(kotlinMethod);
        }

        return new KotlinMethodVisitor(kotlinMethod, myDecompiledKotlinClass.getPackage() + "." + myDecompiledKotlinClass.getName(), myDecompiledKotlinClass.getSuperClass());
    }

    @Override
    public void visitEnd() {
        for (final KotlinMethod method : myDecompiledKotlinClass.getMethods()) {
            myDecompiledKotlinClass.appendImports(method.getImports());
        }
    }

    public String getDecompiledCode() {
        return myDecompiledKotlinClass.toString();
    }
}
