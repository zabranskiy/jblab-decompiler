package KotlinPrinter

import pretty.*
import com.sdc.ast.expressions.Expression
import com.sdc.ast.expressions.Constant
import com.sdc.ast.expressions.BinaryExpression
import com.sdc.ast.expressions.UnaryExpression
import com.sdc.ast.expressions.identifiers.Field
import com.sdc.ast.expressions.identifiers.Variable
import com.sdc.ast.controlflow.Statement
import com.sdc.ast.controlflow.Invocation
import com.sdc.ast.controlflow.Assignment
import com.sdc.ast.controlflow.Return
import com.sdc.ast.controlflow.Throw
import com.sdc.ast.expressions.New
import com.sdc.ast.controlflow.InstanceInvocation
import com.sdc.ast.expressions.NewArray
import com.sdc.kotlin.KotlinClass
import com.sdc.kotlin.KotlinMethod
import com.sdc.kotlin.KotlinClassField
import com.sdc.kotlin.KotlinAnnotation
import com.sdc.ast.expressions.nestedclasses.LambdaFunction

fun printExpression(expression: Expression?, nestSize: Int): PrimeDoc =
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

                left + text(" " + expression.getOperation()) + right
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
                    text(expression.getName())
                else
                    printExpression(expression.getArrayVariable(), nestSize) + text("[") + printExpression(expression.getArrayIndex(), nestSize) + text("]")
            }

            is com.sdc.ast.expressions.Invocation -> {
                var funName : PrimeDoc = text(expression.getFunction() + "(")
                if (expression is com.sdc.ast.expressions.InstanceInvocation) {
                    var variableName = expression.getVariable()!!.getName()
                    if (!variableName.equals("this"))
                        if (variableName.equals("this$"))
                            variableName = "this"
                        funName = text(variableName + ".") + funName
                }

                val args = expression.getArguments()
                if (args!!.isEmpty())
                    funName + text(")")
                else {
                    var argsDocs = args.take(args.size - 1)
                            .map { arg -> printExpression(arg, nestSize) + text(", ") }
                    var arguments = nest(2 * nestSize, fill(argsDocs + printExpression(args.last, nestSize)))

                    funName + arguments + text(")")
                }
            }

            is New -> printExpression(expression.getConstructor(), nestSize)
            is NewArray -> {
                var newArray : PrimeDoc = text("Array<" + expression.getType() + ">(")

                var counter = 0
                for (dimension in expression.getDimensions()!!.toList()) {
                    counter++
                    newArray = newArray + printExpression(dimension, nestSize) + text(", ")
                    if (counter < expression.getDimensions()!!.size())
                        newArray = newArray + text("{ i -> Array<" + expression.getType() + ">(")
                    else
                        newArray = newArray + text("{ i -> null")
                }

                while (counter > 0) {
                    newArray = newArray + text(" })")
                    counter--
                }

                newArray
            }

            is LambdaFunction -> {
                val lambdaFunction = expression.getFunction()
                val arguments = text("{ ") + printMethodParameters(expression.getFunction()) + text(" -> ")
                val body = nest(
                        lambdaFunction!!.getNestSize(),
                        printStatements(lambdaFunction.getBody(), lambdaFunction.getNestSize())
                ) / text("}")
                arguments + body
            }
            else -> throw IllegalArgumentException("Unknown Expression implementer!")
        }

fun printStatement(statement: Statement, nestSize: Int): PrimeDoc =
        when (statement) {
            is Invocation -> {
                var funName : PrimeDoc = text(statement.getFunction() + "(")
                if (statement is InstanceInvocation) {
                    var variableName = statement.getVariable()!!.getName()
                    if (!variableName.equals("this"))
                        if (variableName.equals("this$"))
                            variableName = "this"
                        funName = text(variableName + ".") + funName
                }

                val args = statement.getArguments()
                if (args!!.isEmpty())
                    funName + text(")")
                else {
                    var argsDocs = args.take(args.size - 1)
                            .map { arg -> printExpression(arg, nestSize) + text(", ") }
                    var arguments = nest(2 * nestSize, fill(argsDocs + printExpression(args.last as Expression, nestSize)))

                    funName + arguments + text(")")
                }
            }
            is Assignment -> printExpression(statement.getLeft(), nestSize) + text(" = ") + printExpression(statement.getRight(), nestSize)
            is Return ->
                if (statement.getReturnValue() != null)
                    text("return ") + printExpression(statement.getReturnValue(), nestSize)
                else
                    text("return")
            is Throw -> text("throw") + printExpression(statement.getThrowObject(), nestSize)

            else -> throw IllegalArgumentException("Unknown Statement implementer!")
        }

fun printStatements(statements: List<Statement>?, nestSize: Int): PrimeDoc {
    if ((statements == null) || (statements.size == 0))
        return nil()
    else {
        var body : PrimeDoc = nil()
        for (statement in statements)
            body = body / printStatement(statement, nestSize)
        return body
    }
}

fun printExpressionWithBrackets(expression: Expression, nestSize: Int): PrimeDoc =
        text("(") + printExpression(expression, nestSize) + text(")")

fun printKotlinClass(kotlinClass: KotlinClass): PrimeDoc {
    val packageCode = text("package " + kotlinClass.getPackage())

    var importsCode : PrimeDoc = nil()
    for (importName in kotlinClass.getImports()!!.toArray())
        importsCode = importsCode / text("import " + importName)

    if (kotlinClass.isNormalClass()) {
        var declaration : PrimeDoc = printAnnotations(kotlinClass.getAnnotations()!!.toList()) + text(kotlinClass.getModifier() + kotlinClass.getType() + kotlinClass.getName())

        val genericsDeclaration = kotlinClass.getGenericDeclaration()
        if (!genericsDeclaration!!.isEmpty()) {
            declaration = declaration + text("<")
            var oneType = true
            for (genericType in genericsDeclaration) {
                if (!oneType)
                    declaration = declaration + text(", ")
                declaration = declaration + text(genericType)
                oneType = false
            }
            declaration = declaration + text(">")
        }
        val constructor = kotlinClass.getConstructor()
        if (constructor != null)
            declaration = declaration + printPrimaryConstructorParameters(constructor)

        val superClass = kotlinClass.getSuperClass()
        if (!superClass!!.isEmpty()) {
            declaration = declaration + group(nest(2 * kotlinClass.getNestSize(), line() + text(": " ) + printSuperClassConstructor(kotlinClass.getSuperClassConstructor(), kotlinClass.getNestSize())))
            val traits = kotlinClass.getTraits()!!.toArray()
            for (singleTrait in traits)
                declaration = declaration + text(",") + group(nest(2 * kotlinClass.getNestSize(), line() + text(singleTrait as String)))
        } else {
            var traits = kotlinClass.getTraits()
            if (!traits!!.isEmpty()) {
                declaration = declaration + text(": ") + text(traits!!.get(0))
                for (singleTrait in traits!!.drop(1))
                    declaration = declaration + text(",") + group(nest(2 * kotlinClass.getNestSize(), line() + text(singleTrait)))
            }
        }

        var kotlinClassCode : PrimeDoc = packageCode + importsCode / declaration + text(" {")

        for (classField in kotlinClass.getFields()!!.toList())
            kotlinClassCode = kotlinClassCode + nest(kotlinClass.getNestSize(), line() + printClassField(classField))

        if (constructor != null && !constructor.hasEmptyBody())
            kotlinClassCode = kotlinClassCode + nest(kotlinClass.getNestSize(), line() + printInitialConstructor(kotlinClass.getConstructor()))

        for (classMethod in kotlinClass.getMethods()!!.toList())
            kotlinClassCode = kotlinClassCode + nest(kotlinClass.getNestSize(), line() + printKotlinMethod(classMethod))

        return kotlinClassCode / text("}")
    } else {
        var kotlinCode : PrimeDoc = packageCode + importsCode
        for (method in kotlinClass.getMethods()!!.toList())
            kotlinCode = kotlinCode / printKotlinMethod(method)

        return kotlinCode
    }
}

fun printKotlinMethod(kotlinMethod: KotlinMethod): PrimeDoc {
    var declaration : PrimeDoc = printAnnotations(kotlinMethod.getAnnotations()!!.toList()) + text(kotlinMethod.getModifier() + "fun ")

    val genericsDeclaration = kotlinMethod.getGenericDeclaration()
    if (!genericsDeclaration!!.isEmpty()) {
        declaration = declaration + text("<")
        var oneType = true
        for (genericType in genericsDeclaration) {
            if (!oneType)
                declaration = declaration + text(", ")
            declaration = declaration + text(genericType)
            oneType = false
        }
        declaration = declaration + text("> ")
    }
    declaration = declaration + text(kotlinMethod.getName() + "(")

    val arguments = printMethodParameters(kotlinMethod)

    val body = nest(
            kotlinMethod.getNestSize(),
            printStatements(kotlinMethod.getBody(), kotlinMethod.getNestSize())
    ) / text("}")

    var returnTypeCode = text(" ")
    val returnType = kotlinMethod.getReturnType()
    if (!returnType!!.isEmpty())
        returnTypeCode = text(": " + returnType + " ")

    return declaration + arguments + text(")") + returnTypeCode + text("{") + body
}

fun printClassField(classField: KotlinClassField): PrimeDoc {
    var fieldCode : PrimeDoc = text(classField.getModifier() + "var " + classField.getName() + " : " + classField.getType())
    if (classField.hasInitializer())
        fieldCode = fieldCode + text(" = ") + printExpression(classField.getInitializer(), classField.getNestSize())
    return fieldCode
}

fun printAnnotation(annotation: KotlinAnnotation): PrimeDoc {
    var annotationCode : PrimeDoc = text(annotation.getName())
    val properties = annotation.getProperties()
    if (!properties!!.isEmpty()) {
        annotationCode = annotationCode + text("(")
        var counter = 1
        for ((name, value) in properties) {
            annotationCode = annotationCode + text(name + " = " + if (!annotation.isStringProperty(name)) value else "\"" + value + "\"")
            if (counter < properties.keySet().size)
                annotationCode = annotationCode + text(", ")
            counter++
        }
        annotationCode = annotationCode + text(")")
    }
    return annotationCode + text(" ")
}

fun printAnnotations(annotations: List<KotlinAnnotation>): PrimeDoc {
    var annotationsCode : PrimeDoc = nil()
    for (annotation in annotations)
        annotationsCode = annotationsCode + printAnnotation(annotation)
    return annotationsCode
}

fun printPrimaryConstructorParameters(constructor: KotlinMethod?): PrimeDoc =
    text("(") + printMethodParameters(constructor) + text(")")

fun printMethodParameters(method: KotlinMethod?): PrimeDoc {
    var arguments: PrimeDoc = nil()
    if (method!!.getLastLocalVariableIndex() != 0 || (!method.isNormalClassMethod() && method.getLastLocalVariableIndex() >= 0)) {
        var variables = method.getParameters()!!.toList()
        var index = 0
        for (variable in variables) {
            if (method.checkParameterForAnnotation(index))
                arguments = arguments + printAnnotations(method.getParameterAnnotations(index)!!.toList()) + text(variable)
            else
                arguments = arguments + text(variable)
            if (index + 1 < variables.size)
                arguments = arguments + text(", ")

            index++
        }
    }
    return arguments
}

fun printSuperClassConstructor(superClassConstructor : Expression?, nestSize : Int): PrimeDoc =
        printExpression(superClassConstructor, nestSize)

fun printInitialConstructor(constructor: KotlinMethod?): PrimeDoc {
    val body = nest(
            constructor!!.getNestSize(),
            printStatements(constructor.getBody(), constructor.getNestSize())
    )
    return text("initial constructor {") + body / text("}")
}