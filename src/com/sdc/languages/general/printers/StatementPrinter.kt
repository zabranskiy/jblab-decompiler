package com.sdc.languages.general.printers

import pretty.*

import com.sdc.ast.controlflow.Statement
import com.sdc.ast.controlflow.Assignment
import com.sdc.ast.controlflow.Return
import com.sdc.ast.controlflow.Throw
import com.sdc.ast.controlflow.ExpressionWrapper


abstract class StatementPrinter(printer : ExpressionPrinter) {
    val myExpressionPrinter : ExpressionPrinter = printer

    /***
     * Different types of statements printing
     */
    open fun printStatement(statement: Statement?, nestSize: Int): PrimeDoc =
        when (statement) {
            is ExpressionWrapper -> myExpressionPrinter.printExpression(statement.toExpression(), nestSize)
            is Assignment -> printAssignment(statement, nestSize)
            is Return -> printReturn(statement, nestSize)
            is Throw -> printThrow(statement, nestSize)
            else -> throw IllegalArgumentException("Unknown Statement implementer!")
        }

    open fun printAssignment(statement: Assignment, nestSize: Int): PrimeDoc =
        myExpressionPrinter.printExpression(statement.getLeft(), nestSize) + text(" = ") + myExpressionPrinter.printExpression(statement.getRight(), nestSize)

    open fun printReturn(statement: Return, nestSize: Int): PrimeDoc {
        var returnStatement: PrimeDoc = if (statement.needToPrintReturn()) text("return") else text("")

        if (statement.getReturnValue() != null) {
            returnStatement = returnStatement + text(" ") + myExpressionPrinter.printExpression(statement.getReturnValue(), nestSize)
        }

        return returnStatement
    }

    open fun printThrow(statement: Throw, nestSize: Int): PrimeDoc =
        group(text("throw") + nest(nestSize, line() + myExpressionPrinter.printExpression(statement.getThrowObject(), nestSize)))


    /***
     * Support stuff
     */
    open fun printStatementsDelimiter(): PrimeDoc = text(";")

    open fun printStatements(statements: List<Statement>?, nestSize: Int): PrimeDoc {
        var body: PrimeDoc = nil()

        if (statements != null && statements.size != 0) {
            for (statement in statements) {
                body = body / printStatement(statement, nestSize) + printStatementsDelimiter()
            }
        }

        return body
    }
}

