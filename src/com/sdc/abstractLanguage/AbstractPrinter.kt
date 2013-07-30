package com.sdc.abstractLanguage

import pretty.*
import com.sdc.ast.expressions.Expression
import com.sdc.ast.expressions.Constant
import com.sdc.ast.expressions.BinaryExpression
import com.sdc.ast.expressions.UnaryExpression
import com.sdc.ast.expressions.TernaryExpression
import com.sdc.ast.expressions.identifiers.Field
import com.sdc.ast.expressions.identifiers.Variable
import com.sdc.ast.controlflow.Statement
import com.sdc.ast.controlflow.Invocation
import com.sdc.ast.controlflow.Assignment
import com.sdc.ast.controlflow.Return
import com.sdc.ast.controlflow.Throw
import com.sdc.ast.expressions.New
import com.sdc.ast.controlflow.InstanceInvocation
import com.sdc.ast.expressions.NewArray

abstract class AbstractPrinter {
    open fun printExpression(expression: Expression?, nestSize: Int): PrimeDoc =
        when (expression) {
            is Constant ->
                if (!expression.isStringValue())
                    text(expression.getValue().toString())
                else
                    text("\"" + expression.getValue().toString() + "\"")

            is BinaryExpression -> {
                val opPriority = expression.getPriority()

                val l = expression.getLeft()
                val left = when (l) {
                    is BinaryExpression ->
                        if (opPriority - l.getPriority() < 2)
                            printExpression(l, nestSize)
                        else
                            printExpressionWithBrackets(l, nestSize)
                    else -> printExpression(l, nestSize)
                }

                val r = expression.getRight()
                val right = when (r) {
                    is BinaryExpression ->
                        if (opPriority - r.getPriority() > 0 || opPriority == 3)
                            printExpressionWithBrackets(r, nestSize)
                        else
                            printExpression(r, nestSize)
                    else -> printExpression(r, nestSize)
                }

                group(left / (text(expression.getOperation()) + right))
            }

            is UnaryExpression -> {
                val operand = expression.getOperand()
                val expr = when (operand) {
                    is BinaryExpression ->
                        if (operand.getPriority() < 2)
                            printExpressionWithBrackets(operand, nestSize)
                        else
                            printExpression(operand, nestSize)
                    else -> printExpression(operand, nestSize)
                }
                text(expression.getOperation()) + expr
            }

            is Field -> text(expression.getName())

            is Variable -> {
                if (expression.getArrayIndex() == null)
                    text(printVariableName(expression.getName()))
                else {
                    group(
                            printExpression(expression.getArrayVariable(), nestSize)
                            + text("[") + printExpression(expression.getArrayIndex(), nestSize) + text("]")
                    )
                }
            }

            is com.sdc.ast.expressions.Invocation -> {
                var funName = group(text(expression.getFunction() + "("))
                if (expression is com.sdc.ast.expressions.InstanceInvocation) {
                    var variableName = expression.getVariable()!!.getName()
                    if (!variableName.equals("this")) {
                        variableName =  printVariableName(variableName)
                        funName = group(text(variableName + ".") + funName)
                    }
                }
                val args = expression.getArguments()
                if (args!!.isEmpty())
                    funName + text(")")
                else {
                    var argsDocs = args.take(args.size - 1)
                            .map { arg -> printExpression(arg, nestSize) + text(", ") }
                    var arguments = nest(2 * nestSize, fill(argsDocs + printExpression(args.last, nestSize)))

                    group(funName + arguments + text(")"))
                }
            }

            is New -> group(
                    text("new") + nest(nestSize, line()
                    + printExpression(expression.getConstructor(), nestSize))
            )

            is NewArray -> {
                var newArray = group(text("new") + nest(nestSize, line() + text(expression.getType())))
                for (dimension in expression.getDimensions()!!.toList()) {
                    newArray = group(newArray + text("[") + printExpression(dimension, nestSize) + text("]"))
                }
                newArray
            }

            is TernaryExpression -> {
                val condition = expression.getCondition()
                val printCondition = printExpression(condition, nestSize)
                val leftExp = expression.getLeft()
                val printLeftExp = printExpression(leftExp,nestSize)
                val rightExp = expression.getRight()
                val printRightExp = printExpression(rightExp,nestSize)
                group(nest(nestSize, line() + text("(") + printCondition+text(") ? ") + printLeftExp + text(" : ") + printRightExp))
            }

            else -> throw IllegalArgumentException("Unknown Expression implementer!")
        }

    open fun printStatement(statement: Statement, nestSize: Int): PrimeDoc =
        when (statement) {
            is Invocation -> {
                var funName = group(text(statement.getFunction() + "("))
                if (statement is InstanceInvocation) {
                    var variableName = statement.getVariable()!!.getName()
                    if (!variableName.equals("this")) {
                        variableName = printVariableName(variableName)
                        funName = group(text(variableName + ".") + funName)
                    }
                }
                val args = statement.getArguments()
                if (args!!.isEmpty())
                    funName + text(")")
                else {
                    var argsDocs = args.take(args.size - 1)
                            .map { arg -> printExpression(arg, nestSize) + text(", ") }
                    var arguments = nest(2 * nestSize, fill(argsDocs + printExpression(args.last as Expression, nestSize)))

                    group(funName + arguments + text(")"))
                }
            }
            is Assignment -> group(
                    (printExpression(statement.getLeft(), nestSize) + text(" ="))
                    + nest(nestSize, line() + printExpression(statement.getRight(), nestSize))
            )
            is Return -> if (statement.getReturnValue() != null)
                group(
                        text("return") + nest(nestSize, line()
                        + printExpression(statement.getReturnValue(), nestSize))
                )
            else
                text("return")
            is Throw -> group(
                    text("throw") + nest(nestSize, line()
                    + printExpression(statement.getThrowObject(), nestSize))
            )

            else -> throw IllegalArgumentException("Unknown Statement implementer!")
        }

    open fun printVariableName(variableName : String?): String? = variableName

    open fun printStatementsDelimiter(): PrimeDoc = text(";")

    open fun printStatements(statements: List<Statement>?, nestSize: Int): PrimeDoc {
        if ((statements == null) || (statements.size == 0))
            return nil()
        else {
            var body = printStatement(statements.get(0), nestSize) + printStatementsDelimiter()
            for (i in 1..statements.size - 1) {
                body = body / printStatement(statements.get(i), nestSize) + printStatementsDelimiter()
            }
            return body
        }
    }

    open fun printAnnotation(annotation: AbstractAnnotation): PrimeDoc {
        var annotationCode = group(printAnnotationIdentifier() + text(annotation.getName()))
        val properties = annotation.getProperties()
        if (!properties!!.isEmpty()) {
            annotationCode = group(annotationCode + text("("))
            var counter = 1
            for ((name, value) in properties) {
                annotationCode = group(annotationCode + text(name + " = "
                + if (!annotation.isStringProperty(name)) value else "\"" + value + "\""))
                if (counter < properties.keySet().size)
                    annotationCode = group(annotationCode + text(", "))
                counter++
            }
            annotationCode = group(annotationCode + text(")"))
        }
        return annotationCode
    }

    open fun printAnnotations(annotations: List<AbstractAnnotation>): PrimeDoc {
        var annotationsCode = group(nil())
        for (annotation in annotations)
            annotationsCode = group(annotationsCode + printAnnotation(annotation) + line())
        return annotationsCode
    }

    open fun printExpressionWithBrackets(expression: Expression, nestSize: Int): PrimeDoc =
        text("(") + printExpression(expression, nestSize) + text(")")

    open fun printMethodParameters(method: AbstractMethod?): PrimeDoc {
        var arguments: PrimeDoc = nil()
        if (method!!.getLastLocalVariableIndex() != 0 || (!method.isNormalClassMethod() && method.getLastLocalVariableIndex() >= 0)) {
            var variables = method.getParameters()!!.toList()
            var index = 0
            for (variable in variables) {
                if (method.checkParameterForAnnotation(index))
                    arguments = nest(
                            2 * method.getNestSize()
                            , arguments + printAnnotations(method.getParameterAnnotations(index)!!.toList()) + text(variable)
                    )
                else
                    arguments = nest(
                            2 * method.getNestSize()
                            , arguments + text(variable)
                    )
                if (index + 1 < variables.size)
                    arguments = group(arguments + text(",") + line())

                index++
            }
        }
        return arguments
    }

    abstract fun printAnnotationIdentifier(): PrimeDoc;

    public abstract fun printClass(decompiledClass: AbstractClass): PrimeDoc;

    abstract fun printMethod(decompiledMethod: AbstractMethod): PrimeDoc;

    abstract fun printField(decompiledField: AbstractClassField): PrimeDoc;
}