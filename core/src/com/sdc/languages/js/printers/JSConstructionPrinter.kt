package com.sdc.languages.js.printers

import com.sdc.languages.general.printers.ConstructionPrinter
import com.sdc.languages.general.printers.StatementPrinter
import com.sdc.languages.general.printers.ExpressionPrinter


class JSConstructionPrinter(expressionPrinter : ExpressionPrinter, statementPrinter : StatementPrinter) : ConstructionPrinter(expressionPrinter, statementPrinter) {
}
