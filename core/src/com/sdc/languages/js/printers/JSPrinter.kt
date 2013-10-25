package JSPrinters

import pretty.*

import com.sdc.languages.general.printers.Printer
import com.sdc.languages.general.printers.OperationPrinter
import com.sdc.languages.general.printers.ExpressionPrinter
import com.sdc.languages.general.printers.StatementPrinter
import com.sdc.languages.general.printers.ConstructionPrinter

import com.sdc.languages.general.languageParts.GeneralClass
import com.sdc.languages.general.languageParts.Method
import com.sdc.languages.general.languageParts.ClassField

import com.sdc.languages.js.printers.JSOperationPrinter
import com.sdc.languages.js.printers.JSExpressionPrinter
import com.sdc.languages.js.printers.JSStatementPrinter
import com.sdc.languages.js.printers.JSConstructionPrinter

import com.sdc.languages.js.languageParts.JSClass
import com.sdc.languages.js.languageParts.JSClassField
import com.sdc.languages.js.languageParts.JSMethod

import com.sdc.ast.expressions.identifiers.Variable


class JSPrinter : Printer() {
    /***
     * Support stuff
     */
    override fun createExpressionPrinter(): ExpressionPrinter =
        JSExpressionPrinter(this)

    override fun createStatementPrinter(expressionPrinter: ExpressionPrinter): StatementPrinter =
        JSStatementPrinter(expressionPrinter)

    override fun createConstructionPrinter(expressionPrinter: ExpressionPrinter, statementPrinter: StatementPrinter): ConstructionPrinter =
        JSConstructionPrinter(expressionPrinter, statementPrinter)

    override fun printAnnotationIdentifier(): PrimeDoc = text("")

    override fun printBaseClass(): PrimeDoc = text("Object")

    /***
     * Main overridden printing methods
     */
    override fun printClass(decompiledClass: GeneralClass): PrimeDoc {
        val javaClass: JSClass = decompiledClass as JSClass

        val superClass = javaClass.getSuperClass()
        var declaration = group(nil())
        if (!superClass.isEmpty())
            declaration = group(declaration + nest(javaClass.getNestSize(), line() + text("extends " + superClass)))

        var javaClassCode : PrimeDoc = group(text("function " + javaClass.getName() + "() {"))

        for (classMethod in javaClass.getMethods().toList())
            javaClassCode = javaClassCode + nest(javaClass.getNestSize(), line() + printMethod(classMethod))

        return group(javaClassCode / text("}"))
    }

    override fun printMethod(decompiledMethod: Method): PrimeDoc {
        val classMethod: JSMethod = decompiledMethod as JSMethod

        var declaration = group(text(""))

        declaration = group(declaration + text(classMethod.getName() + "("))

        var throwsExceptions = group(nil())

        var arguments: PrimeDoc = nil()
        if (classMethod.getLastLocalVariableIndex() != 0) {
            var variables = classMethod.getParameters()
            var variablesDocs = variables.take(variables.size - 1)
                    .map { variable -> myExpressionPrinter.printExpression(variable, decompiledMethod.getNestSize()) + text(", ") }

            arguments = nest(2 * classMethod.getNestSize(), fill(variablesDocs + myExpressionPrinter.printExpression(variables.last, decompiledMethod.getNestSize())))
        }

        val body = nest(
                       classMethod.getNestSize(),
                       myConstructionPrinter.printConstruction(classMethod.getBegin(), classMethod.getNestSize())
                       + printMethodError(classMethod)
                   ) / text("}")

        return group(declaration + arguments + text(")") + throwsExceptions / text("{")) + body
    }

    override fun printField(decompiledField: ClassField): PrimeDoc {
        val classField : JSClassField = decompiledField as JSClassField
        return text(classField.getModifier() + classField.getType() + classField.getName() + ";")
    }
}