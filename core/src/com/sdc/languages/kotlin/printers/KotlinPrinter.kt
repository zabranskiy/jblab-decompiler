package KotlinPrinters

import pretty.*

import com.sdc.ast.ExpressionType
import com.sdc.ast.expressions.Expression
import com.sdc.ast.expressions.nestedclasses.LambdaFunction
import com.sdc.ast.expressions.nestedclasses.AnonymousClass
import com.sdc.ast.expressions.InstanceOf
import com.sdc.ast.expressions.Constant
import com.sdc.ast.expressions.UnaryExpression
import com.sdc.ast.expressions.Cast
import com.sdc.ast.expressions.identifiers.Variable
import com.sdc.ast.expressions.identifiers.Field

import com.sdc.ast.controlflow.Assignment

import com.sdc.languages.general.languageParts.GeneralClass
import com.sdc.languages.general.languageParts.Method
import com.sdc.languages.general.languageParts.ClassField

import com.sdc.languages.general.printers.Printer
import com.sdc.languages.general.printers.OperationPrinter
import com.sdc.languages.general.printers.ExpressionPrinter
import com.sdc.languages.general.printers.StatementPrinter
import com.sdc.languages.general.printers.ConstructionPrinter

import com.sdc.languages.kotlin.languageParts.KotlinClass
import com.sdc.languages.kotlin.languageParts.KotlinMethod
import com.sdc.languages.kotlin.languageParts.KotlinClassField

import com.sdc.languages.kotlin.astUtils.KotlinVariable
import com.sdc.languages.kotlin.astUtils.KotlinNewArray

import com.sdc.languages.kotlin.printers.KotlinOperationPrinter
import com.sdc.languages.kotlin.printers.KotlinExpressionPrinter
import com.sdc.languages.kotlin.printers.KotlinStatementPrinter
import com.sdc.languages.kotlin.printers.KotlinConstructionPrinter


class KotlinPrinter: Printer() {
    /***
     * Support stuff
     */
    override fun createExpressionPrinter(): ExpressionPrinter =
        KotlinExpressionPrinter(this)

    override fun createStatementPrinter(expressionPrinter: ExpressionPrinter): StatementPrinter =
        KotlinStatementPrinter(expressionPrinter)

    override fun createConstructionPrinter(expressionPrinter: ExpressionPrinter, statementPrinter: StatementPrinter): ConstructionPrinter =
        KotlinConstructionPrinter(expressionPrinter, statementPrinter)

    override fun printAnnotationIdentifier(): PrimeDoc = text("")

    override fun printBaseClass(): PrimeDoc = text("Any?")


    /***
     * Main overridden printing methods
     */
    override fun printClass(decompiledClass: GeneralClass): PrimeDoc {
        val kotlinClass: KotlinClass = decompiledClass as KotlinClass

        var headerCode : PrimeDoc = printPackageAndImports(decompiledClass) + line()

        if (kotlinClass.isNormalClass()) {
            var declaration : PrimeDoc = printAnnotations(kotlinClass.getAnnotations()!!.toList()) + text(kotlinClass.getModifier() + kotlinClass.getTypeToString() + kotlinClass.getName())

            val genericsCode = printGenerics(kotlinClass.getGenericDeclaration())
            declaration = declaration + genericsCode

            val constructor = kotlinClass.getConstructor()
            if (constructor != null)
                declaration = declaration + printPrimaryConstructorParameters(constructor)

            val superClass = kotlinClass.getSuperClass()
            if (!superClass!!.isEmpty()) {
                declaration = declaration + group(nest(2 * kotlinClass.getNestSize(), line() + text(": " ) + printSuperClassConstructor(kotlinClass.getSuperClassConstructor(), kotlinClass.getNestSize())))
                val traits = kotlinClass.getImplementedInterfaces()!!.toList()
                for (singleTrait in traits)
                    declaration = declaration + text(",") + group(nest(2 * kotlinClass.getNestSize(), line() + text(singleTrait)))
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

    override fun printMethod(decompiledMethod: Method): PrimeDoc {
        val kotlinMethod: KotlinMethod = decompiledMethod as KotlinMethod

        var declaration : PrimeDoc = printAnnotations(kotlinMethod.getAnnotations()!!.toList()) + text(kotlinMethod.getModifier() + "fun ")

        val genericsCode = printGenerics(kotlinMethod.getGenericDeclaration())

        declaration = declaration + genericsCode + text(kotlinMethod.getName() + "(")

        val arguments = printMethodParameters(kotlinMethod)

        val nestedClasses = nest(decompiledMethod.getNestSize(), printMethodInnerClasses(decompiledMethod.getDecompiledClass(), decompiledMethod.getName(), decompiledMethod.getSignature()))

        val body = nest(
                kotlinMethod.getNestSize(),
                myConstructionPrinter.printConstruction(kotlinMethod.getBegin(), kotlinMethod.getNestSize())
                + printMethodError(kotlinMethod)
        ) / text("}")

        val returnTypeCode = printMethodReturnType(kotlinMethod)

        return declaration + arguments + text(")") + returnTypeCode + text("{") + nestedClasses + body
    }

    override fun printField(decompiledField: ClassField): PrimeDoc {
        val kotlinClassField: KotlinClassField = decompiledField as KotlinClassField

        var fieldCode : PrimeDoc = text(kotlinClassField.getModifier() + (if (kotlinClassField.isMutable()) "var " else "val ") + kotlinClassField.getName() + " : " + kotlinClassField.getType())
        if (kotlinClassField.hasInitializer())
            fieldCode = fieldCode + text(" = ") + myExpressionPrinter.printExpression(kotlinClassField.getInitializer(), kotlinClassField.getNestSize())

        return fieldCode
    }


    /***
     * Support stuff
     */
    override fun printVarArgMethodParameter(variable: Variable, nestSize: Int): PrimeDoc {
        variable.declare()

        val myType = variable.getType()
        val typeWithoutOnePairOfBrackets = myType?.getTypeWithOnPairOfBrackets()?.toString(myExpressionPrinter.getOperationPrinter())

        return text("vararg ") + myExpressionPrinter.printExpression(variable.getName(), nestSize) + text(" : " + typeWithoutOnePairOfBrackets?.trim())
    }


    fun printPrimaryConstructorParameters(constructor: Method?): PrimeDoc =
        text("(") + printMethodParameters(constructor) + text(")")

    fun printSuperClassConstructor(superClassConstructor : Expression?, nestSize : Int): PrimeDoc =
        if (superClassConstructor != null)
            myExpressionPrinter.printExpression(superClassConstructor, nestSize)
        else
            text("/* Super class constructor error */")

    fun printInitialConstructor(constructor: Method?): PrimeDoc {
        val body = nest(
            constructor!!.getNestSize(),
            myConstructionPrinter.printConstruction(constructor.getBegin(), constructor.getNestSize())
        )

        return text("initial constructor {") + body / text("}")
    }

    fun printMethodReturnType(method : Method?): PrimeDoc {
        var returnTypeCode = text(" ")

        val returnType = method!!.getReturnType()
        if (!returnType!!.equals("Unit"))
            returnTypeCode = text(": " + returnType + " ")

        return returnTypeCode
    }
}