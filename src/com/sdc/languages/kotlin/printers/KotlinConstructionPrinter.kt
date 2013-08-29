package com.sdc.languages.kotlin.printers

import pretty.*

import com.sdc.languages.general.printers.ConstructionPrinter
import com.sdc.languages.general.printers.StatementPrinter
import com.sdc.languages.general.printers.ExpressionPrinter


class KotlinConstructionPrinter(expressionPrinter : ExpressionPrinter, statementPrinter : StatementPrinter) : ConstructionPrinter(expressionPrinter, statementPrinter) {
    override fun printForEachLieInOperator(): PrimeDoc = text("in")
}
