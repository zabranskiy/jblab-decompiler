package com.sdc.languages.js.printers

import pretty.PrimeDoc

import com.sdc.languages.general.printers.Printer
import com.sdc.languages.general.printers.ExpressionPrinter
import com.sdc.languages.general.printers.OperationPrinter

import com.sdc.ast.expressions.identifiers.Variable


class JSExpressionPrinter(printer : Printer) : ExpressionPrinter(printer) {
    override fun getOperationPrinter(): OperationPrinter {
        return JSOperationPrinter.getInstance() as OperationPrinter;
    }

    override fun printUndeclaredVariable(expression: Variable, nestSize: Int): PrimeDoc =
        printExpression(expression.getName(), nestSize)
}

