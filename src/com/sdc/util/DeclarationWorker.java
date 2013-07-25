package com.sdc.util;

import com.sdc.abstractLanguage.AbstractMethod;
import org.objectweb.asm.Opcodes;

import java.util.*;

public class DeclarationWorker {
    public enum SupportedLanguage {
        JAVA, JAVASCRIPT, KOTLIN
    }

    public static String getAccess(final int access, final SupportedLanguage language)
    {
        switch (language) {
            case JAVA:
                return getJavaAccess(access);
            case KOTLIN:
                return getKotlinAccess(access);
        }
        return "";
    }

    public static String getJavaAccess(final int access) {
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

    public static String getKotlinAccess(final int access) {
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
        if ((access & Opcodes.ACC_FINAL) == 0) {
            sb.append("open ");
        }
        if ((access & Opcodes.ACC_ABSTRACT) != 0) {
            sb.append("abstract ");
        }

        return sb.toString();
    }

    public static String getDescriptor(final String descriptor, final int pos, List<String> imports
            , final SupportedLanguage language)
    {
        switch (language) {
            case JAVA:
                return getJavaDescriptor(descriptor, pos, imports);
            case KOTLIN:
                return getKotlinDescriptor(descriptor, pos, imports);
        }
        return "";
    }

    public static String getJavaDescriptor(final String descriptor, final int pos, List<String> imports) {
        switch (descriptor.charAt(pos)) {
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
            case 'D':
                return "double ";
            case 'L':
                if (descriptor.indexOf("<", pos) == -1) {
                    final String className = descriptor.substring(pos + 1, descriptor.indexOf(";", pos));
                    imports.add(getDecompiledFullClassName(className));
                    return getClassName(className) + " ";
                } else {
                    final String className = descriptor.substring(pos + 1, descriptor.indexOf("<", pos));
                    final String[] genericList = descriptor.substring(descriptor.indexOf("<") + 1, descriptor.indexOf(">")).split(";");
                    imports.add(getDecompiledFullClassName(className));

                    StringBuilder result = new StringBuilder(getClassName(className));
                    boolean isSingleType = true;

                    result.append("<");
                    for (final String generic : genericList) {
                        if (!isSingleType) {
                            result.append(", ");
                        }
                        isSingleType = false;

                        final String genericName = getJavaDescriptor(generic + ";", 0, imports).trim();
                        result.append(genericName);
                    }
                    result.append("> ");

                    return result.toString();
                }
            case 'T':
                return descriptor.substring(pos + 1, descriptor.indexOf(";", pos)) + " ";
            case '+':
                return "? extends " + getJavaDescriptor(descriptor, pos + 1, imports);
            case '-':
                return "? super " + getJavaDescriptor(descriptor, pos + 1, imports);
            case '*':
                return "? ";
            case '[':
                return getJavaDescriptor(descriptor, pos + 1, imports).trim() + "[] ";
            default:
                return "Object ";
        }
    }

    public static String getKotlinDescriptor(final String descriptor, final int pos, List<String> imports) {
        switch (descriptor.charAt(pos)) {
            case 'V':
                return "";
            case 'B':
                return "Byte";
            case 'J':
                return "Long";
            case 'Z':
                return "Boolean";
            case 'I':
                return "Int";
            case 'S':
                return "Short";
            case 'C':
                return "Char";
            case 'F':
                return "Float";
            case 'D':
                return "Double";
            case 'L':
                if (descriptor.indexOf("<", pos) == -1) {
                    final String className = descriptor.substring(pos + 1, descriptor.indexOf(";", pos));
                    imports.add(getDecompiledFullClassName(className));
                    final String actualClassName = getClassName(className);
                    if (isPrimitiveClass(actualClassName)) {
                        return convertJavaPrimitiveClassToKotlin(actualClassName) + "?";
                    } else {
                        return actualClassName;
                    }
                } else {
                    final String className = descriptor.substring(pos + 1, descriptor.indexOf("<", pos));
                    final String[] genericList = descriptor.substring(descriptor.indexOf("<") + 1, descriptor.indexOf(">")).split(";");
                    imports.add(getDecompiledFullClassName(className));

                    StringBuilder result = new StringBuilder(getClassName(className));
                    boolean isSingleType = true;

                    result.append("<");
                    for (final String generic : genericList) {
                        if (!isSingleType) {
                            result.append(", ");
                        }
                        isSingleType = false;

                        final String genericName = getJavaDescriptor(generic + ";", 0, imports).trim();
                        result.append(genericName);
                    }
                    result.append(">");

                    return result.toString();
                }
            case 'T':
                return descriptor.substring(pos + 1, descriptor.indexOf(";", pos));
            case '+':
                return "out " + getKotlinDescriptor(descriptor, pos + 1, imports);
            case '-':
                return "in " + getKotlinDescriptor(descriptor, pos + 1, imports);
            case '*':
                return "? ";
            case '@':
                return getKotlinDescriptor(descriptor, pos + 1, imports);
            //case '[':
            default:
                return "Array<" + getKotlinDescriptor(descriptor, pos + 1, imports) + ">";
        }
    }

    public static int getParametersCount(final String descriptor) {
        int result = 0;
        int pos = 1;
        while (pos < descriptor.indexOf(")")) {
            pos = getNextTypePosition(descriptor, pos);
            result++;
        }
        return result;
    }

    public static void addInformationAboutParameters(final String descriptor, final AbstractMethod abstractMethod
            , final int startIndex, final SupportedLanguage language)
    {
        int count = startIndex - 1;
        int pos = 0;

        while (pos < descriptor.length()) {
            final int backupPos = pos;
            final int backupCount = count;
            final String type = getDescriptor(descriptor, backupPos, abstractMethod.getImports(), language);

            boolean isPrimitiveClass = false;
            switch (descriptor.charAt(pos)) {
                case 'B':
                case 'Z':
                case 'I':
                case 'S':
                case 'C':
                case 'F':
                case 'T':
                case '[':
                case '+':
                case '-':
                case '*':
                    count++;
                    break;
                case 'J':
                case 'D':
                    count += 2;
                    break;
                case 'L':
                    count++;
                    isPrimitiveClass = isPrimitiveClass(type);
                    break;
            }

            pos = getNextTypePosition(descriptor, pos);
            final int index = (count - backupCount) == 1 ? count : count - 1;

            abstractMethod.addLocalVariableName(index, "x" + index);
            if (language == SupportedLanguage.KOTLIN) {
                abstractMethod.addLocalVariableType(index, isPrimitiveClass ? convertJavaPrimitiveClassToKotlin(type) + "?" : type);
            } else {
                abstractMethod.addLocalVariableType(index, type);
            }
        }

        abstractMethod.setLastLocalVariableIndex(count);
    }

    public static String getClassName(final String fullClassName) {
        final String[] classParts = fullClassName.split("/");
        return classParts[classParts.length - 1];
    }

    public static String getDecompiledFullClassName(final String fullClassName) {
        return fullClassName.replace("/", ".");
    }

    public static void parseGenericDeclaration(final String signature, List<String> genericTypesList,
                                         List<String> genericIdentifiersList, List<String> genericTypesImports)
    {
        if (signature != null && signature.indexOf('<') == 0) {
            final String genericDeclaration = signature.substring(signature.indexOf("<") + 1, signature.indexOf(">"));
            final String[] genericTypes = genericDeclaration.split(";");
            for (final String genericType : genericTypes) {
                if (!genericType.isEmpty()) {
                    final String[] genericTypeParts = genericType.split(":");
                    final String genericSuperClassName = genericTypeParts[1].substring(1);
                    genericTypesImports.add(getDecompiledFullClassName(genericSuperClassName));
                    genericTypesList.add(genericSuperClassName);
                    genericIdentifiersList.add(genericTypeParts[0]);
                }
            }
        }
    }

    public static boolean isPrimitiveClass(final String type) {
        Set<String> primitiveTypes = new HashSet<String>(Arrays.asList("Byte", "Long", "Boolean", "Integer", "Int", "Short", "Char", "Float", "Double"));
        return primitiveTypes.contains(type);
    }

    private static String convertJavaPrimitiveClassToKotlin(final String javaClass) {
        if (javaClass.equals("Integer")) {
            return "Int";
        }
        return javaClass;
    }

    private static int getNextTypePosition(final String descriptor, final int startPos) {
        switch (descriptor.charAt(startPos)) {
            case 'B':
            case 'J':
            case 'Z':
            case 'I':
            case 'S':
            case 'C':
            case 'F':
            case 'D':
                return startPos + 1;
            case 'L':
                final int semicolonIndex = descriptor.indexOf(";", startPos);
                final int bracketIndex = descriptor.indexOf("<", startPos);
                if (bracketIndex == -1 || bracketIndex > semicolonIndex) {
                    return semicolonIndex + 1;
                } else {
                    return skipGenericTypePart(descriptor, semicolonIndex) + 1;
                }
            case 'T':
                return descriptor.indexOf(";", startPos) + 1;
            case '[':
            case '+':
            case '-':
            case '*':
            default:
                return getNextTypePosition(descriptor, startPos + 1);
        }
    }

    private static int skipGenericTypePart(final String descriptor, final int startPos) {
        Stack stack = new Stack();
        stack.push(0);
        int pos = startPos + 1;
        while (!stack.isEmpty()) {
            switch (descriptor.charAt(pos)) {
                case '<':
                    stack.push(0);
                    break;
                case '>':
                    stack.pop();
                    break;
            }
            pos++;
        }
        return pos;
    }
}
