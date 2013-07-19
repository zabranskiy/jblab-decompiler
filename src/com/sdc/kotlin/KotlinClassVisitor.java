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
                if (!implInterface.equals("jet/JetObject")) {
                    implementedInterfaces.add(DeclarationWorker.getClassName(implInterface));
                    implementedInterfacesImports.add(DeclarationWorker.getDecompiledFullClassName(implInterface));
                }
            }
        }

        List<String> genericTypesList = new ArrayList<String>();
        List<String> genericIdentifiersList = new ArrayList<String>();
        List<String> genericTypesImports = new ArrayList<String>();
        DeclarationWorker.parseGenericDeclaration(signature, genericTypesList, genericIdentifiersList, genericTypesImports);

        myDecompiledKotlinClass = new KotlinClass(modifier, type, className, packageName.toString(), implementedInterfaces
                , superClass, genericTypesList, genericIdentifiersList, myTextWidth, myNestSize);

        if (!superClassImport.isEmpty()) {
            myDecompiledKotlinClass.appendImport(superClassImport);
        }

        myDecompiledKotlinClass.appendImports(implementedInterfacesImports);
        myDecompiledKotlinClass.appendImports(genericTypesImports);
    }

    @Override
    public void visitSource(final String source, final String debug) {
    }

    @Override
    public void visitOuterClass(final String owner, final String name, final String desc) {
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        KotlinAnnotation annotation = new KotlinAnnotation();
        annotation.setName(DeclarationWorker.getKotlinDescriptor(desc, 0, myDecompiledKotlinClass.getImports()));

        myDecompiledKotlinClass.appendAnnotation(annotation);

        return new KotlinAnnotationVisitor(annotation);
    }

    @Override
    public void visitAttribute(final Attribute attr) {
    }

    @Override
    public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature
            , final Object value) {

        final String description = signature != null ? signature : desc;
        final KotlinClassField cf = new KotlinClassField(DeclarationWorker.getKotlinAccess(access)
                , DeclarationWorker.getKotlinDescriptor(description, 0, myDecompiledKotlinClass.getImports())
                , name, myTextWidth, myNestSize);
        myDecompiledKotlinClass.appendField(cf);
        return null;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc
            , final String signature, final String[] exceptions) {

        final String description = signature != null ? signature : desc;
        final String modifier = DeclarationWorker.getKotlinAccess(access);

        List<String> throwedExceptions = new ArrayList<String>();
        if (exceptions != null) {
            for (final String exception : exceptions) {
                throwedExceptions.add(DeclarationWorker.getClassName(exception));
            }
        }

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
            final int returnTypeIndex = description.indexOf(')') + 1;
            returnType = DeclarationWorker.getKotlinDescriptor(description, returnTypeIndex, myDecompiledKotlinClass.getImports());
            methodName = name;
        }

        final KotlinMethod kotlinMethod = new KotlinMethod(modifier, returnType, methodName
                , throwedExceptions.toArray(new String[throwedExceptions.size()])
                , myDecompiledKotlinClass, genericTypesList, genericIdentifiersList
                , myTextWidth, myNestSize);

        myDecompiledKotlinClass.appendImports(genericTypesImports);

        final String parameters = description.substring(description.indexOf('(') + 1, description.indexOf(')'));
        DeclarationWorker.addInformationAboutParameters(parameters, kotlinMethod, 1, DeclarationWorker.SupportedLanguage.KOTLIN);

        myDecompiledKotlinClass.appendMethod(kotlinMethod);

        return null;
//        return new KotlinMethodVisitor(kotlinMethod
//                , myDecompiledKotlinClass.getPackage() + "." + myDecompiledKotlinClass.getName());
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
