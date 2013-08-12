package com.sdc.abstractLanguage

import pretty.*
import com.sdc.ast.expressions.Expression
import com.sdc.ast.expressions.Constant
import com.sdc.ast.expressions.BinaryExpression
import com.sdc.ast.expressions.UnaryExpression
import com.sdc.ast.expressions.TernaryExpression
import com.sdc.ast.expressions.identifiers.Field
import com.sdc.ast.expressions.identifiers.Variable
import com.sdc.ast.expressions.NewArray
import com.sdc.ast.expressions.New
import com.sdc.ast.expressions.InstanceOf

import com.sdc.ast.controlflow.Statement
import com.sdc.ast.controlflow.Invocation
import com.sdc.ast.controlflow.Assignment
import com.sdc.ast.controlflow.Return
import com.sdc.ast.controlflow.Throw
import com.sdc.ast.controlflow.InstanceInvocation
import com.sdc.ast.expressions.nestedclasses.AnonymousClass
import com.sdc.cfg.nodes.Node
import com.sdc.cfg.nodes.Switch


abstract class AbstractPrinter {
    open fun printExpression(expression: Expression?, nestSize: Int): PrimeDoc =
        when (expression) {
            is Constant ->
                if (!expression.isStringValue())
                    text(expression.getValue().toString())
                else
                    text("\"" + expression.getValue().toString() + "\"")

            is BinaryExpression -> {
                val opPriority = expression.getPriority()

                val l = expression.getLeft()
                val left = when (l) {
                    is BinaryExpression ->
                        if (opPriority - l.getPriority() < 2)
                            printExpression(l, nestSize)
                        else
                            printExpressionWithBrackets(l, nestSize)
                    else -> printExpression(l, nestSize)
                }

                val r = expression.getRight()
                val right = when (r) {
                    is BinaryExpression ->
                        if (opPriority - r.getPriority() > 0 || opPriority == 3)
                            printExpressionWithBrackets(r, nestSize)
                        else
                            printExpression(r, nestSize)
                    else -> printExpression(r, nestSize)
                }

                group(left / (text(expression.getOperation()) + right))
            }

            is UnaryExpression -> {
                val operand = expression.getOperand()
                val expr = when (operand) {
                    is BinaryExpression ->
                        if (operand.getPriority() < 2)
                            printExpressionWithBrackets(operand, nestSize)
                        else
                            printExpression(operand, nestSize)
                    else -> printExpression(operand, nestSize)
                }
                text(expression.getOperation()) + expr
            }

            is Field -> text(expression.getName())

            is Variable -> {
                if (expression.getArrayIndex() == null)
                    text(printVariableName(expression.getName()))
                else {
                    group(
                            printExpression(expression.getArrayVariable(), nestSize)
                            + text("[") + printExpression(expression.getArrayIndex(), nestSize) + text("]")
                    )
                }
            }

            is com.sdc.ast.expressions.Invocation -> {
                var funName = group(text(expression.getFunction()))
                if (expression is com.sdc.ast.expressions.InstanceInvocation) {
                    var variableName = expression.getVariable()!!.getName()
                    if (!variableName.equals("this")) {
                        variableName =  printVariableName(variableName)
                        funName = group(text(variableName + ".") + funName)
                    }
                }
                funName + printInvocationArguments(expression.getArguments(), nestSize)
            }

            is New -> printNewOperator() + printExpression(expression.getConstructor(), nestSize)

            is NewArray -> {
                var newArray = group(text("new") + nest(nestSize, line() + text(expression.getType())))
                for (dimension in expression.getDimensions()!!.toList()) {
                    newArray = group(newArray + text("[") + printExpression(dimension, nestSize) + text("]"))
                }
                newArray
            }

            is InstanceOf -> printExpression(expression.getArgument(), nestSize) + printInstanceOfOperator() + text(expression.getType())

            is AnonymousClass -> printNewOperator() + printAnonymousClass(expression.getNestedClass(), expression.getConstructorArguments())

            is TernaryExpression -> {
                val condition = expression.getCondition()
                val printCondition = printExpression(condition, nestSize)
                val leftExp = expression.getLeft()
                val printLeftExp = printExpression(leftExp,nestSize)
                val rightExp = expression.getRight()
                val printRightExp = printExpression(rightExp,nestSize)
                group(nest(nestSize, line() + text("(") + printCondition+text(") ? ") + printLeftExp + text(" : ") + printRightExp))
            }

            else -> throw IllegalArgumentException("Unknown Expression implementer!")
        }

    open fun printStatement(statement: Statement, nestSize: Int): PrimeDoc =
        when (statement) {
            is Invocation -> {
                var funName = group(text(statement.getFunction()))
                if (statement is InstanceInvocation) {
                    var variableName = statement.getVariable()!!.getName()
                    if (!variableName.equals("this")) {
                        variableName = printVariableName(variableName)
                        funName = group(text(variableName + ".") + funName)
                    }
                }
                funName + printInvocationArguments(statement.getArguments(), nestSize)
            }

            is Assignment -> printExpression(statement.getLeft(), nestSize) + text(" = ") + printExpression(statement.getRight(), nestSize)

            is Return -> {
                var returnStatement : PrimeDoc = if (statement.needToPrintReturn()) text("return ") else text("")
                if (statement.getReturnValue() != null)
                    returnStatement = returnStatement + printExpression(statement.getReturnValue(), nestSize)
                returnStatement
            }

            is Throw -> group(
                    text("throw") + nest(nestSize, line()
                    + printExpression(statement.getThrowObject(), nestSize))
            )

            else -> throw IllegalArgumentException("Unknown Statement implementer!")
        }

    open fun printNode(node : Node?, nestSize : Int): PrimeDoc {
        val startCode = printStatements(node!!.getStatements(), nestSize)
        val breakCode = if (node.isCaseEndNode()) line() + text("break;") else nil()

        val followingCode =
            when (node) {
                is Switch -> {
                    var switchCode : PrimeDoc =  line() + text("switch (") + printExpression(node.getExpr(), nestSize) + text(") {")

                    var keysCode : PrimeDoc = nil()
                    val keys = node.getKeys()
                    for (index in keys!!.indices) {
                        keysCode = keysCode / text("case " + keys[index] + ":") + nest(nestSize, printNode(node.getNodeByKey(index), nestSize))
                    }
                    val defaultBranch = node.getNodeByKey(-1)
                    if (defaultBranch != null) {
                        keysCode = keysCode / text("default:") + nest(nestSize, printNode(defaultBranch, nestSize))
                    }

                    switchCode + nest(nestSize, keysCode) / text("}") + printNode(node.getNextNode(), nestSize)
                }
                else ->
                    if (node.getCondition() == null) {
                        if (node.getListOfTails()!!.isEmpty()) nil() else printNode(node.getListOfTails()!!.get(0), nestSize)
                    } else if (node.getNextNode() != null) {
                        if (node.getListOfTails()!!.get(1).equals(node.getNextNode())) {
                            line() + text("if (!(") + printExpression(node.getCondition(), nestSize) + text(")) {") + nest(nestSize, printNode(node.getListOfTails()!!.get(0), nestSize)) / text("}") + printNode(node.getNextNode(), nestSize)
                        } else {
                            line() + text("if (!(") + printExpression(node.getCondition(), nestSize) + text(")) {") + nest(nestSize, printNode(node.getListOfTails()!!.get(0), nestSize)) / text("} else {") + nest(nestSize, printNode(node.getListOfTails()!!.get(1), nestSize)) / text("}") + printNode(node.getNextNode(), nestSize)
                        }
                    } else text("")
            }

        return startCode + followingCode + breakCode
    }

    open fun printVariableName(variableName : String?): String? = variableName

    open fun printStatementsDelimiter(): PrimeDoc = text(";")

    open fun printInstanceOfOperator(): PrimeDoc = text(" instanceof ")

    open fun printNewOperator(): PrimeDoc = text("new ")

    open fun printStatements(statements: List<Statement>?, nestSize: Int): PrimeDoc {
        var body : PrimeDoc = nil()
        if (statements != null && statements.size != 0)
            for (statement in statements)
                body = body / printStatement(statement, nestSize) + printStatementsDelimiter()
        return body
    }

    open fun printAnnotation(annotation: AbstractAnnotation): PrimeDoc {
        var annotationCode = group(printAnnotationIdentifier() + text(annotation.getName()))
        val properties = annotation.getProperties()
        if (!properties!!.isEmpty()) {
            annotationCode = group(annotationCode + text("("))
            var counter = 1
            for ((name, value) in properties) {
                annotationCode = group(annotationCode + text(name + " = "
                + if (!annotation.isStringProperty(name)) value else "\"" + value + "\""))
                if (counter < properties.keySet().size)
                    annotationCode = group(annotationCode + text(", "))
                counter++
            }
            annotationCode = group(annotationCode + text(")"))
        }
        return annotationCode
    }

    open fun printAnnotations(annotations: List<AbstractAnnotation>): PrimeDoc {
        var annotationsCode = group(nil())
        for (annotation in annotations)
            annotationsCode = group(annotationsCode + printAnnotation(annotation) + line())
        return annotationsCode
    }

    open fun printExpressionWithBrackets(expression: Expression, nestSize: Int): PrimeDoc =
        text("(") + printExpression(expression, nestSize) + text(")")

    open fun printMethodParameters(method: AbstractMethod?): PrimeDoc {
        var arguments: PrimeDoc = nil()
        if (method!!.getLastLocalVariableIndex() != 0 || (!method.isNormalClassMethod() && method.getLastLocalVariableIndex() >= 0)) {
            var variables = method.getParameters()!!.toList()
            var index = 0
            for (variable in variables) {
                if (method.checkParameterForAnnotation(index))
                    arguments = nest(
                            2 * method.getNestSize()
                            , arguments + printAnnotations(method.getParameterAnnotations(index)!!.toList()) + text(variable)
                    )
                else
                    arguments = nest(
                            2 * method.getNestSize()
                            , arguments + text(variable)
                    )
                if (index + 1 < variables.size)
                    arguments = group(arguments + text(",") + line())

                index++
            }
        }
        return arguments
    }

    open fun printInvocationArguments(arguments : List<Expression>?, nestSize : Int): PrimeDoc =
        if (arguments!!.isEmpty())
            text("()")
        else {
            var argsDocs = arguments.take(arguments.size - 1)
                    .map { arg -> printExpression(arg, nestSize) + text(", ") }
            var argumentsCode : PrimeDoc = nest(2 * nestSize, fill(argsDocs + printExpression(arguments.last, nestSize)))

            text("(") + argumentsCode + text(")")
        }

    open fun printGenerics(genericsDeclaration : List<String>?): PrimeDoc {
        var generics : PrimeDoc = nil()
        if (!genericsDeclaration!!.isEmpty()) {
            generics = group(generics + text("<"))
            var oneType = true
            for (genericType in genericsDeclaration) {
                if (!oneType)
                    generics = group(generics + text(", "))
                generics = group(generics + text(genericType))
                oneType = false
            }
            generics = group(generics + text("> "))
        }
        return generics
    }

    open fun printClasses(decompiledClasses : List<AbstractClass>?): PrimeDoc {
        var innerClassesCode : PrimeDoc = nil()
        for (innerClass in decompiledClasses!!.toList()) {
            innerClassesCode = innerClassesCode / printClass(innerClass)
        }
        return innerClassesCode
    }

    open fun printClassBodyInnerClasses(decompiledClass : AbstractClass): PrimeDoc {
        val errorClasses = decompiledClass.getInnerClassesErrors()
        var errorClassesCode : PrimeDoc = nil()
        for ((className, error) in errorClasses)
            errorClassesCode = errorClassesCode / text("// Error occurred while decompiling class " + className + ": " + error.getMessage())

        return errorClassesCode + printClasses(decompiledClass.getClassBodyInnerClasses())
    }

    open fun printMethodInnerClasses(decompiledClass : AbstractClass?, methodName : String?, descriptor : String?): PrimeDoc {
        return printClasses(decompiledClass!!.getMethodInnerClasses(methodName, descriptor))
    }

    open fun printPackageAndImports(decompiledClass : AbstractClass?): PrimeDoc =
        if (!decompiledClass!!.isNestedClass()) {
            val packageCode = text("package " + decompiledClass.getPackage() + ";") + line()
            var imports = group(nil())
            for (importName in decompiledClass.getImports()!!.toArray())
                imports = group(imports + text("import " + importName + ";") + line())
            packageCode + imports
        } else {
            nil()
        }

    open fun printAnonymousClass(anonymousClass : AbstractClass?, arguments : List<Expression>?): PrimeDoc {
        val superClassName = anonymousClass!!.getSuperClass()
        val declaration =
                if (superClassName!!.isEmpty()) {
                    val implementedInterfaces = anonymousClass.getImplementedInterfaces()
                    val declaration =
                        if (implementedInterfaces!!.isEmpty())
                            printBaseClass()
                        else
                            text(implementedInterfaces.get(0))
                    declaration + text("() {")
                } else {
                    text(superClassName) + printInvocationArguments(arguments, anonymousClass.getNestSize())
                }

        var anonClassCode : PrimeDoc = declaration + nest(anonymousClass.getNestSize(), printClassBodyInnerClasses(anonymousClass))

        for (classField in anonymousClass.getFields()!!.toList())
            anonClassCode = anonClassCode + nest(anonymousClass.getNestSize(), line() + printField(classField))

        for (classMethod in anonymousClass.getMethods()!!.toList())
            anonClassCode = anonClassCode + nest(anonymousClass.getNestSize(), line() + printMethod(classMethod))

        return anonClassCode / text("}")
    }

    open fun printMethodError(decompiledMethod : AbstractMethod?): PrimeDoc {
        val error = decompiledMethod!!.getError()
        return if (error != null) line() + text("// " + error.getErrorLocation() + ": " + error.getException()!!.getMessage()) else nil()
    }

    abstract fun printBaseClass(): PrimeDoc;

    abstract fun printAnnotationIdentifier(): PrimeDoc;

    abstract fun printClass(decompiledClass: AbstractClass): PrimeDoc;

    abstract fun printMethod(decompiledMethod: AbstractMethod): PrimeDoc;

    abstract fun printField(decompiledField: AbstractClassField): PrimeDoc;
}