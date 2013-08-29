package com.sdc.languages.kotlin.printers

import pretty.*

import com.sdc.languages.general.printers.Printer
import com.sdc.languages.general.printers.ExpressionPrinter
import com.sdc.languages.general.printers.OperationPrinter

import com.sdc.languages.general.languageParts.GeneralClass

import com.sdc.ast.expressions.Expression
import com.sdc.ast.expressions.Cast
import com.sdc.ast.expressions.InstanceOf
import com.sdc.ast.expressions.identifiers.Field
import com.sdc.ast.expressions.identifiers.Variable
import com.sdc.ast.expressions.Constant
import com.sdc.ast.expressions.nestedclasses.AnonymousClass
import com.sdc.ast.expressions.nestedclasses.LambdaFunction

import com.sdc.languages.kotlin.astUtils.KotlinNewArray
import com.sdc.languages.kotlin.astUtils.KotlinVariable

import KotlinPrinters.KotlinPrinter


class KotlinExpressionPrinter(printer : Printer) : ExpressionPrinter(printer) {
    override fun getOperationPrinter(): OperationPrinter {
        return KotlinOperationPrinter.getInstance() as OperationPrinter;
    }


    /***
     * Different types of expressions printing
     */
    override fun printExpression(expression: Expression?, nestSize: Int): PrimeDoc =
        when (expression) {
            is KotlinNewArray -> {
                if (expression.getInitializer() != null) {
                    var newArray : PrimeDoc = text("Array<" + expression.getType() + ">(")

                    newArray = newArray + printExpression(expression.getDimensions()!!.get(0), nestSize) + text(", ") + printExpression(expression.getInitializer(), nestSize) + text(")")

                    newArray
                } else {
                    text("Array<" + expression.getType() + ">(") + printExpression(expression.getDimensions()!!.get(0), nestSize) + text(", i -> null)")
                }
            }

            is LambdaFunction -> {
                val lambdaFunction = expression.getFunction()
                val returnTypeCode = (myPrinter as KotlinPrinter).printMethodReturnType(lambdaFunction)
                val arguments = text("{ (") + myPrinter.printMethodParameters(lambdaFunction) + text(")") + returnTypeCode + text("-> ")

                val body = nest(
                        lambdaFunction!!.getNestSize(),
                        myPrinter.myConstructionPrinter.printConstruction(lambdaFunction.getBegin(), lambdaFunction.getNestSize())
                ) / text("}")

                arguments + body
            }

            is AnonymousClass -> text("object : ") + super<ExpressionPrinter>.printExpression(expression, nestSize)

            else -> super<ExpressionPrinter>.printExpression(expression, nestSize)
        }

    override fun printVariable(expression: Variable, nestSize: Int): PrimeDoc =
        if (expression.getName() is Constant && (expression.getName() as Constant).getValue().toString().equals("this$"))
            text("this")
        else
            super.printVariable(expression, nestSize)

    override fun printCast(expression: Cast, nestSize: Int): PrimeDoc {
        val opPriority = expression.getPriority(getOperationPrinter())
        val isAssociative = expression.isAssociative();
        val operand = expression.getOperand()
        val expr = printExpressionCheckBrackets(operand, opPriority, isAssociative, nestSize);

        return  expr + text(expression.getOperation(getOperationPrinter()))
    }

    override fun printField(expression: Field, nestSize: Int): PrimeDoc {
        val owner = expression.getOwner()

        if (owner != null && checkForSharedVar(owner)) {
            return printExpression(owner, nestSize)
        } else {
            return super.printField(expression, nestSize)
        }
    }


    /***
     * Support stuff
     */
    override fun printVariableName(variableName: String?): String? = if (variableName.equals("this$")) "this" else variableName

    override fun printInstanceOfOperator(): PrimeDoc = text("is ")

    override fun printNewOperator(): PrimeDoc = text("")

    override fun printUndeclaredVariable(expression: Variable, nestSize: Int): PrimeDoc =
        printExpression(expression.getName(), nestSize) + text(" : ") + printType(expression.getType(),nestSize)

    override fun printInvertedInstanceOf(expression : InstanceOf, nestSize : Int): PrimeDoc =
        printInstanceOfArgument(expression, nestSize) + text("!") + printInstanceOfOperator() + printType(expression.getType(),nestSize)

    override fun printAnonymousClassDeclaration(anonymousClass: GeneralClass?, arguments: List<Expression>?): PrimeDoc {
        var declaration : PrimeDoc = nil()

        val hasDefaultSuperClass = anonymousClass!!.getSuperClass()!!.isEmpty()
        if (!hasDefaultSuperClass) {
            declaration = declaration + text(anonymousClass.getSuperClass()) + printInvocationArguments(arguments, anonymousClass.getNestSize())
        }

        val implementedInterfaces = anonymousClass.getImplementedInterfaces()

        if (!implementedInterfaces!!.isEmpty()) {
            declaration = declaration + (if (!hasDefaultSuperClass) text(", ") else nil()) + text(implementedInterfaces.get(0))
            for (interface in implementedInterfaces.drop(1))
                declaration = declaration + text(", ") + text(interface)
        } else if (hasDefaultSuperClass) {
            declaration = myPrinter.printBaseClass() + text("()")
        }

        return declaration + text(" {")
    }

    fun checkForSharedVar(expression : Expression?): Boolean =
        expression is Field && KotlinVariable.isSharedVar(expression.getType()?.toString(KotlinOperationPrinter.getInstance()))
            || expression is KotlinVariable && KotlinVariable.isSharedVar(expression.getActualType()?.toString(KotlinOperationPrinter.getInstance()))
}
