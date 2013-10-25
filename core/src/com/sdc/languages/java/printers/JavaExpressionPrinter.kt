package com.sdc.languages.java.printers

import com.sdc.languages.general.printers.Printer
import com.sdc.languages.general.printers.ExpressionPrinter
import com.sdc.languages.general.printers.OperationPrinter


class JavaExpressionPrinter(printer : Printer) : ExpressionPrinter(printer) {
    override fun getOperationPrinter(): OperationPrinter {
        return JavaOperationPrinter.getInstance();
    }
}