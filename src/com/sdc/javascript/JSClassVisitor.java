package com.sdc.javascript;

import com.sdc.abstractLangauge.AbstractClassVisitor;
import com.sdc.javascript.expressions.*;
import com.sdc.javascript.statements.ExpressionStatement;
import com.sdc.javascript.statements.Statement;
import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.objectweb.asm.Opcodes.ASM4;

public class JSClassVisitor extends AbstractClassVisitor {
    //private JavaClass myDecompiledJavaClass = null;
    private JSClass myDecompiledJSClass = null;
    private final int myTextWidth;
    private final int myNestSize;

    public JSClassVisitor(final int textWidth, final int nestSize) {
        super(ASM4);
        this.myTextWidth = textWidth;
        this.myNestSize = nestSize;
    }

    @Override
    public void visit(final int version, final int access, final String name
            , final String signature,final  String superName, final String[] interfaces) {
        final String modifier = getAccess(access & ~Opcodes.ACC_SUPER);
        String type = "";

        if ((access & Opcodes.ACC_ENUM) == 0
                && (access & Opcodes.ACC_INTERFACE) == 0
                && (access & Opcodes.ACC_ANNOTATION) == 0)
        {
            type = "class ";
        }

        String superClass = "";
        if (superName != null && !"java/lang/Object".equals(superName)) {
            superClass = superName;
        }

        List<String> implementedInterfaces = new ArrayList<String>();
        if (interfaces != null && interfaces.length > 0) {
            implementedInterfaces.addAll(Arrays.asList(interfaces));
        }

        /*myDecompiledJavaClass = new JavaClass(modifier, type, name, implementedInterfaces
                , superClass, myTextWidth, myNestSize);*/

        myDecompiledJSClass = new JSClass(modifier, type, name, implementedInterfaces,
                superClass, myTextWidth, myNestSize);
    }

    @Override
    public void visitSource(final String source, final String debug) {
    }

    @Override
    public void visitOuterClass(final String owner, final String name, final String desc) {
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        return null;
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
        final JSClassField cf = new JSClassField(getAccess(access)
                , getDescriptor(desc.charAt(0)), name, myTextWidth, myNestSize);
        myDecompiledJSClass.appendField(cf);
        return null;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc
            , final String signature, final String[] exceptions) {
        if (name.equals("<init>")) {
            return null;
        }

        final String modifier = getAccess(access);
        final String returnType = getDescriptor(desc.charAt(desc.indexOf(')') + 1));
        final int lastLocalVariableIndex = getLastLocalVariableIndex(desc.substring(1, desc.indexOf(')')));

        final JSClassMethod jsClassMethod = new JSClassMethod(modifier, returnType, name, exceptions
                , lastLocalVariableIndex, myTextWidth, myNestSize);

        myDecompiledJSClass.appendMethod(jsClassMethod);

        return new JSMethodVisitor(jsClassMethod);
    }

    @Override
    public void visitEnd() {
        System.out.println(myDecompiledJSClass.toString());
    }

    private String getAccess(final int access) {
        StringBuilder sb = new StringBuilder("");

        if ((access & Opcodes.ACC_PUBLIC) != 0) {
            sb.append("public ");
        }
        if ((access & Opcodes.ACC_PRIVATE) != 0) {
            sb.append("private ");
        }
        if ((access & Opcodes.ACC_PROTECTED) != 0) {
            sb.append("protected ");
        }
        if ((access & Opcodes.ACC_FINAL) != 0) {
            sb.append("final ");
        }
        if ((access & Opcodes.ACC_STATIC) != 0) {
            sb.append("static ");
        }
        if ((access & Opcodes.ACC_SYNCHRONIZED) != 0) {
            sb.append("synchronized ");
        }
        if ((access & Opcodes.ACC_VOLATILE) != 0) {
            sb.append("volatile ");
        }
        if ((access & Opcodes.ACC_TRANSIENT) != 0) {
            sb.append("transient ");
        }
        if ((access & Opcodes.ACC_ABSTRACT) != 0) {
            sb.append("abstract ");
        }
        if ((access & Opcodes.ACC_STRICT) != 0) {
            sb.append("strictfp ");
        }
        if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
            sb.append("synthetic ");
        }
        if ((access & Opcodes.ACC_ENUM) != 0) {
            sb.append("enum ");
        }

        return sb.toString();
    }

    private String getDescriptor(final char descriptor) {
        switch (descriptor) {
            case 'V':
                return "void ";
            case 'B':
                return "byte ";
            case 'J':
                return "long ";
            case 'Z':
                return "boolean ";
            case 'I':
                return "int ";
            case 'S':
                return "short ";
            case 'C':
                return "char ";
            case 'F':
                return "float ";
            // case 'D':
            default:
                return "double ";
        }
    }

    private int getLastLocalVariableIndex(final String methodDescription) {
        int count = 0;

        for (char descriptor : methodDescription.toCharArray()) {
            switch (descriptor) {
                case 'B':
                    count++;
                    break;
                case 'J':
                    count += 2;
                    break;
                case 'Z':
                    count++;
                    break;
                case 'I':
                    count++;
                    break;
                case 'S':
                    count++;
                    break;
                case 'C':
                    count++;
                    break;
                case 'F':
                    count++;
                    break;
                case 'D':
                    count += 2;
                    break;
            }
        }

        return count;
    }
}