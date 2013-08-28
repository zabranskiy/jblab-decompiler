package JSPrinters

import pretty.*

import com.sdc.languages.general.printers.AbstractPrinter
import com.sdc.languages.general.printers.AbstractOperationPrinter

import com.sdc.languages.general.languageParts.AbstractClass
import com.sdc.languages.general.languageParts.AbstractMethod
import com.sdc.languages.general.languageParts.AbstractClassField

import com.sdc.languages.js.printers.JSOperationPrinter

import com.sdc.languages.js.languageParts.JSClass
import com.sdc.languages.js.languageParts.JSClassField
import com.sdc.languages.js.languageParts.JSMethod

import com.sdc.ast.expressions.identifiers.Variable


class JSPrinter : AbstractPrinter() {
    override fun getOperationPrinter():AbstractOperationPrinter{
        return JSOperationPrinter.getInstance() as AbstractOperationPrinter;
    }
    override fun printAnnotationIdentifier(): PrimeDoc = text("")

    override fun printBaseClass(): PrimeDoc = text("Object")

    override fun printUndeclaredVariable(expression: Variable, nestSize: Int): PrimeDoc =
        printExpression(expression.getName(), nestSize)

    override fun printClass(decompiledClass: AbstractClass): PrimeDoc {
        val javaClass: JSClass = decompiledClass as JSClass

        val superClass = javaClass.getSuperClass()
        var declaration = group(nil())
        if (!superClass!!.isEmpty())
            declaration = group(declaration + nest(javaClass.getNestSize(), line() + text("extends " + superClass)))

        var javaClassCode : PrimeDoc = group(text("function " + javaClass.getName() + "() {"))

        for (classMethod in javaClass.getMethods()!!.toList())
            javaClassCode = javaClassCode + nest(javaClass.getNestSize(), line() + printMethod(classMethod))

        return group(javaClassCode / text("}"))
    }

    override fun printMethod(decompiledMethod: AbstractMethod): PrimeDoc {
        val classMethod: JSMethod = decompiledMethod as JSMethod

        var declaration = group(text(""))

        declaration = group(declaration + text(classMethod.getName() + "("))

        var throwsExceptions = group(nil())

        var arguments: PrimeDoc = nil()
        if (classMethod.getLastLocalVariableIndex() != 0) {
            var variables = classMethod.getParameters()
            var variablesDocs = variables!!.take(variables!!.size - 1)
                    .map { variable -> printExpression(variable, decompiledMethod.getNestSize()) + text(", ") }

            arguments = nest(2 * classMethod.getNestSize(), fill(variablesDocs + printExpression(variables!!.last, decompiledMethod.getNestSize())))
        }

        val body = nest(
                       classMethod.getNestSize(),
                       printConstruction(classMethod.getBegin(), classMethod.getNestSize())
                       + printMethodError(classMethod)
                   ) / text("}")

        return group(declaration + arguments + text(")") + throwsExceptions / text("{")) + body
    }

    override fun printField(decompiledField: AbstractClassField): PrimeDoc {
        val classField: JSClassField = decompiledField as JSClassField
        return text(classField.getModifier() + classField.getType() + classField.getName() + ";")
    }
}