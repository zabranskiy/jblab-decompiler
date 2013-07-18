package JavaClassPrinter

import pretty.*
import com.sdc.ast.expressions.Expression
import com.sdc.ast.expressions.Constant
import com.sdc.ast.expressions.BinaryExpression
import com.sdc.ast.expressions.UnaryExpression
import com.sdc.ast.expressions.TernaryExpression
import com.sdc.ast.expressions.identifiers.Field
import com.sdc.ast.expressions.identifiers.Variable
import com.sdc.ast.controlflow.Statement
import com.sdc.ast.controlflow.Invocation
import com.sdc.ast.controlflow.Assignment
import com.sdc.ast.controlflow.Return
import com.sdc.java.JavaClass
import com.sdc.java.JavaClassField
import com.sdc.java.JavaClassMethod
import com.sdc.ast.controlflow.Throw
import com.sdc.ast.expressions.New
import com.sdc.ast.controlflow.InstanceInvocation
import com.sdc.ast.expressions.NewArray
import com.sdc.java.JavaAnnotation


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
                text(expression.getName())
            else {
                group(
                    printExpression(expression.getArrayVariable(), nestSize)
                    + text("[") + printExpression(expression.getArrayIndex(), nestSize) + text("]")
                )
            }
        }

        is com.sdc.ast.expressions.Invocation -> {
            var funName = group(text(expression.getFunction() + "("))
            if (expression is InstanceInvocation) {
                funName = group(text(expression.getVariable()!!.getName() + ".") + funName)
            }
            val args = expression.getArguments()
            if (args!!.isEmpty())
                funName + text(")")
            else {
                var argsDocs = args.take(args.size - 1)
                        .map { arg -> printExpression(arg, nestSize) + text(", ") }
                var arguments = nest(2 * nestSize, fill(argsDocs + printExpression(args.last, nestSize)))

                group(funName + arguments + text(")"))
            }
        }

        is New -> group(
                      text("new") + nest(nestSize, line()
                      + printExpression(expression.getConstructor(), nestSize))
                  )
        is NewArray -> {
            var newArray = group(text("new") + nest(nestSize, line() + text(expression.getType())))
            for (dimension in expression.getDimensions()!!.toList()) {
                newArray = group(newArray + text("[") + printExpression(dimension, nestSize) + text("]"))
            }
            newArray
        }


        is TernaryExpression -> {
            val condition = expression.getCondition()
            val printCondition = printExpression(condition, nestSize)
            val leftExp = expression.getLeft()
            val printLeftExp = printExpression(leftExp,nestSize)
            val rightExp = expression.getRight()
            val printRightExp = printExpression(rightExp,nestSize)
            group(nest(nestSize, line()+text("(") + printCondition+text(") ? ")+printLeftExp+text(" : ")+printRightExp))
        }
        else -> throw IllegalArgumentException("Unknown Expression implementer!")
    }

fun printStatement(statement: Statement, nestSize: Int): PrimeDoc =
    when (statement) {
        is Invocation -> {
            var funName = group(text(statement.getFunction() + "("))
            if (statement is InstanceInvocation) {
                funName = group(text(statement.getVariable()!!.getName() + ".") + funName)
            }
            val args = statement.getArguments()
            if (args!!.isEmpty())
                funName + text(")")
            else {
                var argsDocs = args.take(args.size - 1)
                        .map { arg -> printExpression(arg, nestSize) + text(", ") }
                var arguments = nest(2 * nestSize, fill(argsDocs + printExpression(args.last as Expression, nestSize)))

                group(funName + arguments + text(")"))
            }
        }
        is Assignment -> group(
                             (printExpression(statement.getLeft(), nestSize) + text(" ="))
                             + nest(nestSize, line() + printExpression(statement.getRight(), nestSize))
                         )
        is Return -> if (statement.getReturnValue() != null)
                         group(
                            text("return") + nest(nestSize, line()
                            + printExpression(statement.getReturnValue(), nestSize))
                         )
                     else
                         text("return")
        is Throw -> group(
                        text("throw") + nest(nestSize, line()
                        + printExpression(statement.getThrowObject(), nestSize))
                    )

        else -> throw IllegalArgumentException("Unknown Statement implementer!")
    }

fun printStatements(statements: List<Statement>?, nestSize: Int): PrimeDoc {
    if ((statements == null) || (statements.size == 0))
        return nil()
    else {
        var body = printStatement(statements.get(0), nestSize) + text(";")
        for (i in 1..statements.size - 1) {
            body = group(body / printStatement(statements.get(i), nestSize) + text(";"))
        }
        return body
    }
}

fun printExpressionWithBrackets(expression: Expression, nestSize: Int): PrimeDoc =
    text("(") + printExpression(expression, nestSize) + text(")")

fun printJavaClass(javaClass: JavaClass): PrimeDoc {
    val packageCode = text("package " + javaClass.getPackage() + ";")
    var imports = group(nil())
    for (importName in javaClass.getImports()!!.toArray())
        imports = group(imports / text("import " + importName + ";"))

    var declaration = group(printAnnotations(javaClass.getAnnotations()!!.toList()) / text(javaClass.getModifier() + javaClass.getType() + javaClass.getName()))

    val genericsDeclaration = javaClass.getGenericDeclaration()
    if (!genericsDeclaration!!.isEmpty()) {
        declaration = group(declaration + text("<"))
        var oneType = true
        for (genericType in genericsDeclaration) {
            if (!oneType)
                declaration = group(declaration + text(", "))
            declaration = group(declaration + text(genericType))
            oneType = false
        }
        declaration = group(declaration + text(">"))
    }

    val superClass = javaClass.getSuperClass()
    if (!superClass!!.isEmpty())
        declaration = group(declaration + nest(javaClass.getNestSize(), line() + text("extends " + superClass)))

    val implementedInterfaces = javaClass.getImplementedInterfaces()!!.toArray()
    if (!implementedInterfaces.isEmpty())
        declaration = group(
                declaration
                + nest(2 * javaClass.getNestSize(), line() + text("implements " + implementedInterfaces.get(0)))
        )
    for (interface in implementedInterfaces.drop(1)) {
        declaration = group(
                (declaration + text(","))
                + nest(2 * javaClass.getNestSize(), line() + text(interface as String))
        )
    }

    var javaClassCode = group(packageCode + imports + declaration + text(" {"))

    for (classField in javaClass.getFields()!!.toArray())
        javaClassCode = group(
                javaClassCode
                + nest(javaClass.getNestSize(), line() + printClassField(classField as JavaClassField))
        )

    for (classMethod in javaClass.getMethods()!!.toArray())
        javaClassCode = group(
                javaClassCode
                + nest(javaClass.getNestSize(), printClassMethod(classMethod as JavaClassMethod))
        )

    return group(javaClassCode / text("}"))
}

fun printClassMethod(classMethod: JavaClassMethod): PrimeDoc {
    var declaration = group(printAnnotations(classMethod.getAnnotations()!!.toList()) / text(classMethod.getModifier()))

    val genericsDeclaration = classMethod.getGenericDeclaration()
    if (!genericsDeclaration!!.isEmpty()) {
        declaration = group(declaration + text("<"))
        var oneType = true
        for (genericType in genericsDeclaration) {
            if (!oneType)
                declaration = group(declaration + text(", "))
            declaration = group(declaration + text(genericType))
            oneType = false
        }
        declaration = group(declaration + text("> "))
    }
    declaration = group(declaration + text(classMethod.getReturnType() + classMethod.getName() + "("))

    var throwsExceptions = group(nil())
    val exceptions = classMethod.getExceptions()
    if (!exceptions!!.isEmpty()) {
        throwsExceptions = group(text("throws " + exceptions.get(0)))
        for (exception in exceptions.drop(1)) {
            throwsExceptions = group((throwsExceptions + text(",")) / text(exception))
        }
        throwsExceptions = group(nest(2 * classMethod.getNestSize(), line() + throwsExceptions))
    }

    var arguments: PrimeDoc = nil()
    if (classMethod.getLastLocalVariableIndex() != 0) {
        var variables = classMethod.getParameters()!!.toList()
        var index = 0
        for (variable in variables) {
            if (classMethod.checkParameterForAnnotation(index))
                arguments = nest(
                        2 * classMethod.getNestSize()
                        , arguments + printAnnotations(classMethod.getParameterAnnotations(index)!!.toList()) + text(" " + variable)
                )
            else
                arguments = nest(
                        2 * classMethod.getNestSize()
                        , arguments + text(variable)
                )
            if (index + 1 < variables.size)
                arguments = group(arguments + text(",") + line())

            index++
        }
    }

    val body = nest(
                   classMethod.getNestSize(),
                   line() + printStatements(classMethod.getBody(), classMethod.getNestSize())
               ) / text("}")

    return group(declaration + arguments + text(")") + throwsExceptions / text("{")) + body
}

fun printClassField(classField: JavaClassField): PrimeDoc =
    text(classField.getModifier() + classField.getType() + classField.getName() + ";")

fun printAnnotation(annotation: JavaAnnotation): PrimeDoc {
    var annotationCode = group(text("@" + annotation.getName()))
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

fun printAnnotations(annotations: List<JavaAnnotation>): PrimeDoc {
    var annotationsCode = group(nil())
    for (annotation in annotations)
        annotationsCode = group(annotationsCode / printAnnotation(annotation))
    return annotationsCode
}