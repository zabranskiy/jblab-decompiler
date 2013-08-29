package com.sdc.languages.java.printers

import com.sdc.languages.general.printers.ConstructionPrinter
import com.sdc.languages.general.printers.StatementPrinter
import com.sdc.languages.general.printers.ExpressionPrinter


class JavaConstructionPrinter(expressionPrinter : ExpressionPrinter, statementPrinter : StatementPrinter) : ConstructionPrinter(expressionPrinter, statementPrinter) {
}