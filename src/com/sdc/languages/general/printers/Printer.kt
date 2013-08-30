package com.sdc.languages.general.printers

import pretty.*
import com.decompiler.Decompiler

import com.sdc.languages.general.languageParts.GeneralClass
import com.sdc.languages.general.languageParts.Method
import com.sdc.languages.general.languageParts.ClassField
import com.sdc.languages.general.languageParts.Annotation

import com.sdc.ast.expressions.identifiers.Variable


abstract class Printer {
    val myExpressionPrinter : ExpressionPrinter = createExpressionPrinter()
    val myStatementPrinter : StatementPrinter = createStatementPrinter(myExpressionPrinter)
    val myConstructionPrinter : ConstructionPrinter = createConstructionPrinter(myExpressionPrinter, myStatementPrinter)


    /***
     * General class printing methods
     */
    abstract fun printClass(decompiledClass: GeneralClass): PrimeDoc

    abstract fun printMethod(decompiledMethod: Method): PrimeDoc

    abstract fun printField(decompiledField: ClassField): PrimeDoc

    abstract fun printAnnotationIdentifier(): PrimeDoc

    abstract fun printBaseClass(): PrimeDoc

    abstract fun createExpressionPrinter(): ExpressionPrinter

    abstract fun createStatementPrinter(expressionPrinter : ExpressionPrinter): StatementPrinter

    abstract fun createConstructionPrinter(expressionPrinter : ExpressionPrinter, statementPrinter : StatementPrinter): ConstructionPrinter


    /***
     * Support stuff
     */

    open fun printPackageAndImports(decompiledClass: GeneralClass?): PrimeDoc =
        if (!decompiledClass!!.isNestedClass()) {
            val packageCode = text("package " + decompiledClass.getPackage() + ";") + line()
            var imports = group(nil())
            for (importName in decompiledClass.getImports()!!.toArray())
                imports = group(imports + text("import " + importName + ";") + line())
            packageCode / imports
        } else {
            nil()
        }

    open fun printAnnotations(annotations: List<Annotation>): PrimeDoc {
        var annotationsCode = group(nil())
        for (annotation in annotations)
            annotationsCode = group(annotationsCode + printAnnotation(annotation) + line())
        return annotationsCode
    }

    open fun printAnnotation(annotation: Annotation): PrimeDoc {
        var annotationCode = group(printAnnotationIdentifier() + text(annotation.getName()))

        val properties = annotation.getProperties()
        if (!properties!!.isEmpty()) {
            annotationCode = group(annotationCode + text("("))

            var counter = 1
            for ((name, value) in properties) {
                annotationCode = group(annotationCode + text(name + " = " + if (!annotation.isStringProperty(name)) value else "\"" + value + "\""))

                if (counter < properties.keySet().size) {
                    annotationCode = group(annotationCode + text(", "))
                }

                counter++
            }
            annotationCode = group(annotationCode + text(")"))
        }

        return annotationCode
    }

    open fun printMethodParameters(method: Method?): PrimeDoc {
        var arguments: PrimeDoc = nil()

        if (method!!.getLastLocalVariableIndex() > 0 || ((!method.isNormalClassMethod() || method.getModifier()!!.contains("static")) && method.getLastLocalVariableIndex() >= 0)) {
            val allVariables = method.getParameters()!!.toList()
            val variables = if (method.getDecompiledClass()!!.isNestedClass() && method.isConstructor()) allVariables.tail else allVariables
            var index = 0

            for (i in variables.indices) {
                val variableName =
                    if (i == (variables.size() - 1)) {
                        printLastMethodParameter(variables[i], method.getNestSize());
                    } else {
                        myExpressionPrinter.printExpression(variables[i], method.getNestSize())
                    }

                if (method.checkParameterForAnnotation(index))
                    arguments = arguments + printAnnotations(method.getParameterAnnotations(index)!!.toList()) + variableName
                else
                    arguments = arguments + variableName

                if (index + 1 < variables.size) {
                    arguments = group(arguments + text(",") + line())
                }

                index++
            }
        }

        return group(nest(2 * method.getNestSize(), arguments))
    }

    open fun printLastMethodParameter(variable: Variable, nestSize: Int): PrimeDoc {
        val myType = variable.getType()

        if (myType?.isArray() as Boolean) {
            return printVarArgMethodParameter(variable, nestSize)
        } else {
            return myExpressionPrinter.printExpression(variable, nestSize)
        }
    }

    open fun printVarArgMethodParameter(variable: Variable, nestSize: Int): PrimeDoc =
        myExpressionPrinter.printExpression(variable, nestSize)

    open fun printGenerics(genericsDeclaration: List<String>?): PrimeDoc {
        var generics: PrimeDoc = nil()

        if (!genericsDeclaration!!.isEmpty()) {
            generics = group(generics + text("<"))

            var oneType = true
            for (genericType in genericsDeclaration) {
                if (!oneType) {
                    generics = group(generics + text(", "))
                }

                generics = group(generics + text(genericType))
                oneType = false
            }

            generics = group(generics + text("> "))
        }

        return generics
    }

    open fun printClasses(decompiledClasses: List<GeneralClass>?): PrimeDoc {
        var innerClassesCode: PrimeDoc = nil()
        for (innerClass in decompiledClasses!!.toList()) {
            innerClassesCode = innerClassesCode / printClass(innerClass)
        }
        return innerClassesCode
    }

    open fun printClassBodyInnerClasses(decompiledClass: GeneralClass): PrimeDoc {
        val errorClasses = decompiledClass.getInnerClassesErrors()

        var errorClassesCode: PrimeDoc = nil()
        for ((className, error) in errorClasses)
            errorClassesCode = errorClassesCode / text("// Error occurred while decompiling class " + className + ": " + Decompiler.printExceptionToString(error))

        return errorClassesCode + printClasses(decompiledClass.getClassBodyInnerClasses())
    }

    open fun printMethodInnerClasses(decompiledClass: GeneralClass?, methodName: String?, descriptor: String?): PrimeDoc {
        return printClasses(decompiledClass!!.getMethodInnerClasses(methodName, descriptor))
    }

    open fun printMethodError(decompiledMethod: Method?): PrimeDoc {
        val error = decompiledMethod!!.getError()
        return if (error != null) line() + text("// " + error.getErrorLocation() + ":") + text(error.getException()) else nil()
    }
}