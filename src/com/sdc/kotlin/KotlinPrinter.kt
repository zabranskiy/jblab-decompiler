package KotlinPrinter

import pretty.*
import com.sdc.ast.expressions.Expression
import com.sdc.ast.expressions.Constant
import com.sdc.ast.expressions.BinaryExpression
import com.sdc.ast.expressions.UnaryExpression
import com.sdc.ast.expressions.New
import com.sdc.ast.expressions.NewArray
import com.sdc.ast.expressions.identifiers.Field
import com.sdc.ast.expressions.identifiers.Variable
import com.sdc.ast.expressions.nestedclasses.LambdaFunction

import com.sdc.ast.controlflow.Statement
import com.sdc.ast.controlflow.Invocation
import com.sdc.ast.controlflow.Assignment
import com.sdc.ast.controlflow.Return
import com.sdc.ast.controlflow.Throw
import com.sdc.ast.controlflow.InstanceInvocation

import com.sdc.kotlin.KotlinClass
import com.sdc.kotlin.KotlinMethod
import com.sdc.kotlin.KotlinClassField
import com.sdc.kotlin.KotlinAnnotation


import com.sdc.abstractLanguage.AbstractClass
import com.sdc.abstractLanguage.AbstractMethod
import com.sdc.abstractLanguage.AbstractClassField
import com.sdc.abstractLanguage.AbstractAnnotation
import com.sdc.abstractLanguage.AbstractPrinter

class KotlinPrinter: AbstractPrinter() {
    override fun printVariableName(variableName: String?): String? = if (variableName.equals("this$")) "this" else variableName

    override fun printStatementsDelimiter(): PrimeDoc = text("")

    override fun printAnnotationIdentifier(): PrimeDoc = text("")

    override fun printExpression(expression: Expression?, nestSize: Int): PrimeDoc =
        when (expression) {
            is New -> printExpression(expression.getConstructor(), nestSize)

            is NewArray -> {
                var newArray : PrimeDoc = text("Array<" + expression.getType() + ">(")

                var counter = 0
                for (dimension in expression.getDimensions()!!.toList()) {
                    counter++
                    newArray = newArray + printExpression(dimension, nestSize) + text(", ")
                    if (counter < expression.getDimensions()!!.size())
                        newArray = newArray + text("{ i -> Array<" + expression.getType() + ">(")
                    else
                        newArray = newArray + text("{ i -> null")
                }

                while (counter > 0) {
                    newArray = newArray + text(" })")
                    counter--
                }

                newArray
            }

            is LambdaFunction -> {
                val lambdaFunction = expression.getFunction()
                val arguments = text("{ ") + printMethodParameters(lambdaFunction) + text(" -> ")
                val body = nest(
                        lambdaFunction!!.getNestSize(),
                        printStatements(lambdaFunction.getBody(), lambdaFunction.getNestSize())
                ) / text("}")
                arguments + body
            }

            else -> super<AbstractPrinter>.printExpression(expression, nestSize)
        }

    override fun printClass(decompiledClass: AbstractClass): PrimeDoc {
        val kotlinClass: KotlinClass = decompiledClass as KotlinClass

        val packageCode = text("package " + kotlinClass.getPackage())

        var importsCode : PrimeDoc = nil()
        for (importName in kotlinClass.getImports()!!.toArray())
            importsCode = importsCode / text("import " + importName)

        if (kotlinClass.isNormalClass()) {
            var declaration : PrimeDoc = printAnnotations(kotlinClass.getAnnotations()!!.toList()) + text(kotlinClass.getModifier() + kotlinClass.getType() + kotlinClass.getName())

            val genericsCode = printGenerics(kotlinClass.getGenericDeclaration())
            declaration = declaration + genericsCode

            val constructor = kotlinClass.getConstructor()
            if (constructor != null)
                declaration = declaration + printPrimaryConstructorParameters(constructor)

            val superClass = kotlinClass.getSuperClass()
            if (!superClass!!.isEmpty()) {
                declaration = declaration + group(nest(2 * kotlinClass.getNestSize(), line() + text(": " ) + printSuperClassConstructor(kotlinClass.getSuperClassConstructor(), kotlinClass.getNestSize())))
                val traits = kotlinClass.getImplementedInterfaces()!!.toArray()
                for (singleTrait in traits)
                    declaration = declaration + text(",") + group(nest(2 * kotlinClass.getNestSize(), line() + text(singleTrait as String)))
            } else {
                var traits = kotlinClass.getImplementedInterfaces()
                if (!traits!!.isEmpty()) {
                    declaration = declaration + text(": ") + text(traits!!.get(0))
                    for (singleTrait in traits!!.drop(1))
                        declaration = declaration + text(",") + group(nest(2 * kotlinClass.getNestSize(), line() + text(singleTrait)))
                }
            }

            var kotlinClassCode : PrimeDoc = packageCode + importsCode / declaration + text(" {")

            for (classField in kotlinClass.getFields()!!.toList())
                kotlinClassCode = kotlinClassCode + nest(kotlinClass.getNestSize(), line() + printField(classField))

            if (constructor != null && !(constructor as KotlinMethod).hasEmptyBody())
                kotlinClassCode = kotlinClassCode + nest(kotlinClass.getNestSize(), line() + printInitialConstructor(kotlinClass.getConstructor()))

            for (classMethod in kotlinClass.getMethods()!!.toList())
                kotlinClassCode = kotlinClassCode + nest(kotlinClass.getNestSize(), line() + printMethod(classMethod))

            return kotlinClassCode / text("}")
        } else {
            var kotlinCode : PrimeDoc = packageCode + importsCode
            for (method in kotlinClass.getMethods()!!.toList())
                kotlinCode = kotlinCode / printMethod(method)

            return kotlinCode
        }
    }

    override fun printMethod(decompiledMethod: AbstractMethod): PrimeDoc {
        val kotlinMethod: KotlinMethod = decompiledMethod as KotlinMethod

        var declaration : PrimeDoc = printAnnotations(kotlinMethod.getAnnotations()!!.toList()) + text(kotlinMethod.getModifier() + "fun ")

        val genericsCode = printGenerics(kotlinMethod.getGenericDeclaration())

        declaration = declaration + genericsCode + text(kotlinMethod.getName() + "(")

        val arguments = printMethodParameters(kotlinMethod)

        val body = nest(
                kotlinMethod.getNestSize(),
                printStatements(kotlinMethod.getBody(), kotlinMethod.getNestSize())
        ) / text("}")

        var returnTypeCode = text(" ")
        val returnType = kotlinMethod.getReturnType()
        if (!returnType!!.isEmpty())
            returnTypeCode = text(": " + returnType + " ")

        return declaration + arguments + text(")") + returnTypeCode + text("{") + body
    }

    override fun printField(decompiledField: AbstractClassField): PrimeDoc {
        val kotlinClassField: KotlinClassField = decompiledField as KotlinClassField

        var fieldCode : PrimeDoc = text(kotlinClassField.getModifier() + "var " + kotlinClassField.getName() + " : " + kotlinClassField.getType())
        if (kotlinClassField.hasInitializer())
            fieldCode = fieldCode + text(" = ") + printExpression(kotlinClassField.getInitializer(), kotlinClassField.getNestSize())
        return fieldCode
    }

    fun printPrimaryConstructorParameters(constructor: AbstractMethod?): PrimeDoc =
        text("(") + printMethodParameters(constructor) + text(")")

    fun printSuperClassConstructor(superClassConstructor : Expression?, nestSize : Int): PrimeDoc =
            printExpression(superClassConstructor, nestSize)

    fun printInitialConstructor(constructor: AbstractMethod?): PrimeDoc {
        val body = nest(
                constructor!!.getNestSize(),
                printStatements(constructor.getBody(), constructor.getNestSize())
        )
        return text("initial constructor {") + body / text("}")
    }
}