package JavaPrinter

import pretty.*

import com.sdc.java.JavaClass
import com.sdc.java.JavaClassField
import com.sdc.java.JavaMethod
import com.sdc.java.JavaAnnotation

import com.sdc.abstractLanguage.AbstractClass
import com.sdc.abstractLanguage.AbstractMethod
import com.sdc.abstractLanguage.AbstractClassField
import com.sdc.abstractLanguage.AbstractAnnotation
import com.sdc.abstractLanguage.AbstractPrinter


class JavaPrinter: AbstractPrinter() {
    override fun printAnnotationIdentifier(): PrimeDoc = text("@")

    override fun printClass(decompiledClass: AbstractClass): PrimeDoc {
        val javaClass: JavaClass = decompiledClass as JavaClass

        var headerCode : PrimeDoc = printPackageAndImports(decompiledClass)

        var declaration : PrimeDoc = group(printAnnotations(javaClass.getAnnotations()!!.toList()) + text(javaClass.getModifier() + javaClass.getType() + javaClass.getName()))

        val genericsCode = printGenerics(javaClass.getGenericDeclaration())
        declaration = declaration + genericsCode

        val superClass = javaClass.getSuperClass()
        if (!superClass!!.isEmpty())
            declaration = group(declaration + nest(javaClass.getNestSize(), line() + text("extends " + superClass)))

        val implementedInterfaces = javaClass.getImplementedInterfaces()!!.toArray()
        if (!implementedInterfaces.isEmpty())
            declaration = group(
                    declaration
                    + nest(2 * javaClass.getNestSize(), line() + text("implements " + implementedInterfaces.get(0)))
            )
        for (interface in implementedInterfaces.drop(1)) {
            declaration = group(
                    (declaration + text(","))
                    + nest(2 * javaClass.getNestSize(), line() + text(interface as String))
            )
        }

        var javaClassCode : PrimeDoc = group(headerCode + declaration + text(" {")) + nest(javaClass.getNestSize(), printClassBodyInnerClasses(javaClass))

        for (classField in javaClass.getFields()!!.toList())
            javaClassCode = javaClassCode + nest(javaClass.getNestSize(), line() + printField(classField))

        for (classMethod in javaClass.getMethods()!!.toList())
            javaClassCode = javaClassCode + nest(javaClass.getNestSize(), line() + printMethod(classMethod))


        return javaClassCode / text("}")
    }

    override fun printMethod(decompiledMethod: AbstractMethod): PrimeDoc {
        val classMethod: JavaMethod = decompiledMethod as JavaMethod

        var declaration : PrimeDoc = group(printAnnotations(classMethod.getAnnotations()!!.toList()) + text(classMethod.getModifier()))

        val genericsCode = printGenerics(classMethod.getGenericDeclaration())

        declaration = group(declaration + genericsCode + text(classMethod.getReturnType() + classMethod.getName() + "("))

        var throwsExceptions = group(nil())
        val exceptions = classMethod.getExceptions()
        if (!exceptions!!.isEmpty()) {
            throwsExceptions = group(text("throws " + exceptions.get(0)))
            for (exception in exceptions.drop(1)) {
                throwsExceptions = group((throwsExceptions + text(",")) / text(exception))
            }
            throwsExceptions = group(nest(2 * classMethod.getNestSize(), line() + throwsExceptions))
        }

        var arguments: PrimeDoc = printMethodParameters(classMethod)

        val nestedClasses = nest(classMethod.getNestSize(), printMethodInnerClasses(classMethod.getDecompiledClass(), classMethod.getName(), classMethod.getSignature()))

        val body = nest(
                       classMethod.getNestSize(),
                       printStatements(classMethod.getBody(), classMethod.getNestSize())
                   ) / text("}")

        return group(declaration + arguments + text(")") + throwsExceptions + text(" {")) + nestedClasses + body
    }

    override fun printField(decompiledField: AbstractClassField): PrimeDoc {
        val classField: JavaClassField = decompiledField as JavaClassField

        var fieldCode : PrimeDoc = text(classField.getModifier() + classField.getType() + classField.getName())
        if (classField.hasInitializer())
            fieldCode = fieldCode + text(" = ") + printExpression(classField.getInitializer(), classField.getNestSize())
        return fieldCode + text(";")
    }
}