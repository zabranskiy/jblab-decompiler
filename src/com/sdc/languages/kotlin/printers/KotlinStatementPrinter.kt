package com.sdc.languages.kotlin.printers

import pretty.*

import com.sdc.languages.general.printers.StatementPrinter
import com.sdc.languages.general.printers.ExpressionPrinter

import com.sdc.ast.expressions.Expression
import com.sdc.ast.expressions.Constant
import com.sdc.ast.expressions.identifiers.Field

import com.sdc.ast.controlflow.Assignment

import com.sdc.languages.kotlin.astUtils.KotlinVariable


class KotlinStatementPrinter(printer : ExpressionPrinter) : StatementPrinter(printer) {
    override fun printStatementsDelimiter(): PrimeDoc = text("")

    override fun printAssignment(statement: Assignment, nestSize: Int): PrimeDoc {
        if ((myExpressionPrinter as KotlinExpressionPrinter).checkForSharedVar(statement.getLeft())
                && statement.getRight() is Constant
                && (statement.getRight() as Constant).getValue().toString().equals("null"))
        {
            return nil()
        } else {
            return super.printAssignment(statement, nestSize)
        }
    }
}
