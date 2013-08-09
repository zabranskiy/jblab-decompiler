package JSPrinter

import pretty.*
import com.sdc.js.JSClass
import com.sdc.js.JSClassField
import com.sdc.js.JSClassMethod
import com.sdc.abstractLanguage.AbstractClass
import com.sdc.abstractLanguage.AbstractMethod
import com.sdc.abstractLanguage.AbstractClassField
import com.sdc.abstractLanguage.AbstractPrinter
import com.sdc.abstractLanguage.AbstractOperationPrinter
import com.sdc.js.JSOperationPrinter

class JSPrinter : AbstractPrinter() {
    override fun getOperationPrinter():AbstractOperationPrinter{
        return JSOperationPrinter.getInstance() as AbstractOperationPrinter;
    }
    override fun printAnnotationIdentifier(): PrimeDoc = text("")

    override fun printBaseClass(): PrimeDoc = text("Object")

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
        val classMethod: JSClassMethod = decompiledMethod as JSClassMethod

        var declaration = group(text(""))

        declaration = group(declaration + text(classMethod.getName() + "("))

        var throwsExceptions = group(nil())

        var arguments: PrimeDoc = nil()
        if (classMethod.getLastLocalVariableIndex() != 0) {
            var variables = classMethod.getParameters()
            var variablesDocs = variables!!.take(variables!!.size - 1)
                    .map { variable -> text(variable) + text(", ") }

            arguments = nest(2 * classMethod.getNestSize(), fill(variablesDocs + text(variables!!.last as String)))
        }

        val body = nest(
                       classMethod.getNestSize(),
                       printStatements(classMethod.getBody()!!, classMethod.getNestSize())
                       + printMethodError(classMethod)
                   ) / text("}")

        return group(declaration + arguments + text(")") + throwsExceptions / text("{")) + body
    }

    override fun printField(decompiledField: AbstractClassField): PrimeDoc {
        val classField: JSClassField = decompiledField as JSClassField
        return text(classField.getModifier() + classField.getType() + classField.getName() + ";")
    }
}