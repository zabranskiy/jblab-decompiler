package KotlinPrinter

import pretty.*
import com.sdc.ast.expressions.Expression
import com.sdc.ast.expressions.NewArray
import com.sdc.ast.expressions.nestedclasses.LambdaFunction
import com.sdc.ast.expressions.nestedclasses.AnonymousClass
import com.sdc.kotlin.KotlinClass
import com.sdc.kotlin.KotlinMethod
import com.sdc.kotlin.KotlinClassField
import com.sdc.abstractLanguage.AbstractClass
import com.sdc.abstractLanguage.AbstractMethod
import com.sdc.abstractLanguage.AbstractClassField
import com.sdc.abstractLanguage.AbstractPrinter
import com.sdc.abstractLanguage.AbstractOperationPrinter
import com.sdc.kotlin.KotlinOperationPrinter
import com.sdc.cfg.nodes.Node
import com.sdc.cfg.nodes.Switch

class KotlinPrinter: AbstractPrinter() {
    override fun getOperationPrinter(): AbstractOperationPrinter{
        return KotlinOperationPrinter.getInstance() as AbstractOperationPrinter;
    }
    override fun printVariableName(variableName: String?): String? = if (variableName.equals("this$")) "this" else variableName

    override fun printStatementsDelimiter(): PrimeDoc = text("")

    override fun printAnnotationIdentifier(): PrimeDoc = text("")

    override fun printInstanceOfOperator(): PrimeDoc = text(" is ")

    override fun printNewOperator(): PrimeDoc = text("")

    override fun printBaseClass(): PrimeDoc = text("Any?")

    override fun printExpression(expression: Expression?, nestSize: Int): PrimeDoc =
        when (expression) {
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
                val returnTypeCode = printMethodReturnType(lambdaFunction)
                val arguments = text("{ (") + printMethodParameters(lambdaFunction) + text(")") + returnTypeCode + text("-> ")
                val body = nest(
                        lambdaFunction!!.getNestSize(),
                          printConstruction(lambdaFunction.getBegin(), lambdaFunction.getNestSize())
                ) / text("}")
                arguments + body
            }

            is AnonymousClass -> text("object : ") + super<AbstractPrinter>.printExpression(expression, nestSize)

            else -> super<AbstractPrinter>.printExpression(expression, nestSize)
        }

    override fun printClass(decompiledClass: AbstractClass): PrimeDoc {
        val kotlinClass: KotlinClass = decompiledClass as KotlinClass

        var headerCode : PrimeDoc = printPackageAndImports(decompiledClass)

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

            var kotlinClassCode : PrimeDoc = headerCode + declaration + text(" {") + nest(kotlinClass.getNestSize(), printClassBodyInnerClasses(kotlinClass))

            for (classField in kotlinClass.getFields()!!.toList())
                kotlinClassCode = kotlinClassCode + nest(kotlinClass.getNestSize(), line() + printField(classField))

            if (constructor != null && !(constructor as KotlinMethod).hasEmptyBody())
                kotlinClassCode = kotlinClassCode + nest(kotlinClass.getNestSize(), line() + printInitialConstructor(kotlinClass.getConstructor()))

            for (classMethod in kotlinClass.getMethods()!!.toList())
                kotlinClassCode = kotlinClassCode + nest(kotlinClass.getNestSize(), line() + printMethod(classMethod))

            return kotlinClassCode / text("}")
        } else {
            var kotlinCode : PrimeDoc = headerCode
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

        val nestedClasses = nest(decompiledMethod.getNestSize(), printMethodInnerClasses(decompiledMethod.getDecompiledClass(), decompiledMethod.getName(), decompiledMethod.getSignature()))

        val body = nest(
                kotlinMethod.getNestSize(),
                printConstruction(kotlinMethod.getBegin(), kotlinMethod.getNestSize())
                + printMethodError(kotlinMethod)
        ) / text("}")

        val returnTypeCode = printMethodReturnType(kotlinMethod)

        return declaration + arguments + text(")") + returnTypeCode + text("{") + nestedClasses + body
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
            if (superClassConstructor != null)
                printExpression(superClassConstructor, nestSize)
            else
                text("/* Super class constructor error */")

    fun printInitialConstructor(constructor: AbstractMethod?): PrimeDoc {
        val body = nest(
                constructor!!.getNestSize(),
                printConstruction(constructor.getBegin(), constructor.getNestSize())
        )
        return text("initial constructor {") + body / text("}")
    }

    fun printMethodReturnType(method : AbstractMethod?): PrimeDoc {
        var returnTypeCode = text(" ")
        val returnType = method!!.getReturnType()
        if (!returnType!!.equals("Unit"))
            returnTypeCode = text(": " + returnType + " ")
        return returnTypeCode
    }
}