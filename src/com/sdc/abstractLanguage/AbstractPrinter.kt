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
import com.sdc.ast.expressions.ExprIncrement
import com.sdc.ast.controlflow.Increment
import com.sdc.cfg.constructions.Construction
import com.sdc.cfg.constructions.ElementaryBlock
import com.sdc.cfg.constructions.ConditionalBlock
import com.sdc.cfg.constructions.While
import com.sdc.cfg.constructions.DoWhile
import com.sdc.cfg.constructions.For
import com.sdc.cfg.constructions.ForEach
import com.sdc.cfg.constructions.TryCatch
import com.sdc.cfg.constructions.When
import com.sdc.cfg.constructions.Switch
import com.sdc.ast.expressions.PriorityExpression
import com.sdc.ast.OperationType

abstract class AbstractPrinter {
    abstract fun getOperationPrinter(): AbstractOperationPrinter;

    open fun printExpression(expression: Expression?, nestSize: Int): PrimeDoc =
        when (expression) {
            is Constant ->
                if (!expression.isStringValue())
                    text(expression.getValue().toString())
                else
                    text("\"" + expression.getValue().toString() + "\"")

            is BinaryExpression -> {
                val l = expression.getLeft()
                val r = expression.getRight()
                val opPriority = expression.getPriority(getOperationPrinter())
                val left = printExpressionCheckBrackets(l,opPriority,nestSize);
                val right = printExpressionCheckBrackets(r,opPriority,nestSize);
                group(left / (text(expression.getOperation(getOperationPrinter())) + right))
             }

            is UnaryExpression -> {
                val opPriority = expression.getPriority(getOperationPrinter())
                val operand = expression.getOperand()
                val expr = printExpressionCheckBrackets(operand,opPriority,nestSize);
                text(expression.getOperation(getOperationPrinter())) + expr
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
                val l = expression.getLeft()
                val r = expression.getRight()
                var c=  expression.getCondition();
                val opPriority = expression.getPriority(getOperationPrinter())
                val condition = printExpressionCheckBrackets(c,opPriority,nestSize);
                val left = printExpressionCheckBrackets(l,opPriority,nestSize);
                val right = printExpressionCheckBrackets(r,opPriority,nestSize);
                group(nest(nestSize, line() + text("(") + condition+text(") ? ") + left + text(" : ") + right))
            }

            is ExprIncrement -> {
                val expr = expression.getOperand();
                val printExpr = printExpression(expr,nestSize)
                var myType= expression.getOperationType();
                var operation=expression.getOperation(getOperationPrinter());
                if(myType==OperationType.INC_REV || myType==OperationType.DEC_REV){
                    group(nest(nestSize, line() + text(operation) + printExpr))
                }   else{
                    group(nest(nestSize, line() + printExpr + text(operation)))
                }
            }

            else -> throw IllegalArgumentException("Unknown Expression implementer!")
        }

    open fun printExpressionCheckBrackets(expression: Expression?, nextOpPriority: Int, nestSize: Int): PrimeDoc =
        when (expression) {
            is PriorityExpression ->
                if (nextOpPriority < expression.getPriority(getOperationPrinter()) )
                    printExpressionWithBrackets(expression, nestSize)
                else
                    printExpression(expression, nestSize)
            else -> printExpression(expression, nestSize)
        }

    open fun printStatement(statement: Statement?, nestSize: Int): PrimeDoc =
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

            is Increment ->{
                    group(text(statement.getName())+text(statement.getOperation()));
            }

            else -> throw IllegalArgumentException("Unknown Statement implementer!")
        }

    open fun printConstruction(construction : Construction?, nestSize : Int): PrimeDoc {
        if (construction == null)
            return nil()
        else {
            val breakCode =
                    if (construction.hasBreak())
                        line() + text("break") + (if (construction.hasBreakToLabel()) text(" " + construction.getBreak()) else nil()) + printStatementsDelimiter()
                    else
                        nil()

            val continueCode =
                    if (construction.hasContinue())
                        line() + text("continue") + (if (construction.hasContinueToLabel()) text(" " + construction.getContinue()) else nil()) + printStatementsDelimiter()
                    else
                        nil()

            val mainCode =
                when (construction) {
                    is ElementaryBlock -> printStatements(construction.getStatements(), nestSize)

                    is ConditionalBlock -> {
                        val thenPart = printConstruction(construction.getThenBlock(), nestSize)

                        var elsePart : PrimeDoc = nil()
                        if (construction.hasElseBlock()) {
                            elsePart = text(" else {") + nest(nestSize, line() + printConstruction(construction.getElseBlock(), nestSize)) / text("}")
                        }

                        text("if (!(") + printExpression(construction.getCondition(), nestSize) + text(")) {") + nest(nestSize, line() + thenPart) / text("}") + nest(nestSize, line() + elsePart)
                    }

                    is While -> {
                        val body = printConstruction(construction.getBody(), nestSize)
                        text("while (!(") + printExpression(construction.getCondition(), nestSize) + text(")) {") + nest(nestSize, line() + body) / text("}")
                    }

                    is DoWhile -> {
                        val body = text("do {") + nest(nestSize, line() + printConstruction(construction.getBody(), nestSize)) / text("}")
                        body / text("while (!(") + printExpression(construction.getCondition(), nestSize) + text("))")
                    }

                    is For -> {
                        val body = printConstruction(construction.getBody(), nestSize)
                        val initialization = printStatement(construction.getVariableInitialization(), nestSize)
                        val afterThought = printStatement(construction.getAfterThought(), nestSize)
                        text("for (") + initialization + text(", !(") + printExpression(construction.getCondition(), nestSize) + text("), ") + afterThought + text(") {") + nest(nestSize, line() + body) / text("}")
                    }

                    is ForEach -> {
                        val body = printConstruction(construction.getBody(), nestSize)
                        text("for (") + printExpression(construction.getVariable(), nestSize) + text(" : ") + printExpression(construction.getContainer(), nestSize) + text(") {") + nest(nestSize, line() + body) / text("}")
                    }

                    is TryCatch -> {
                        val body = printConstruction(construction.getTryBody(), nestSize)

                        var catchBlockCode : PrimeDoc = nil()
                        for ((variable, catchBody) in construction.getCatches()!!.entrySet()) {
                            catchBlockCode = catchBlockCode / text("catch (") + printExpression(variable, nestSize) + text(") {") + nest(nestSize, line() + printConstruction(catchBody, nestSize)) + text("}")
                        }

                        val finallyCode =
                                if (construction.hasFinallyBlock())
                                    text("finally {") + nest(nestSize, line() + printConstruction(construction.getFinallyBody(), nestSize)) / text("}")
                                else
                                    nil()

                        text("try {") + nest(nestSize, line() + body) / text("}") + catchBlockCode / finallyCode
                    }

                    is Switch -> {
                        var switchCode : PrimeDoc =  text("switch (") + printExpression(construction.getCondition(), nestSize) + text(") {")

                        var keysCode : PrimeDoc = nil()
                        for ((key, caseBody) in construction.getCases()!!.entrySet()) {
                            keysCode = keysCode + nest(nestSize, line() + text("case " + key + ":") + nest(nestSize, printConstruction(caseBody, nestSize)))
                        }

                        val defaultCaseCode =
                                if (construction.hasDefaultCase())
                                    nest(nestSize, line() + text("default:") + nest(nestSize, printConstruction(construction.getDefaultCase(), nestSize)))
                                else
                                    nil()

                        switchCode + keysCode + defaultCaseCode / text("}")
                    }

                    is When -> {
                        var whenCode : PrimeDoc = text("when (") + printExpression(construction.getCondition(), nestSize) + text(") {")

                        var keysCode : PrimeDoc = nil()
                        for ((key, caseBody) in construction.getCases()!!.entrySet()) {
                            keysCode = keysCode / printExpression(key, nestSize) + text(" -> ") + nest(nestSize, line() + printConstruction(caseBody, nestSize))
                        }

                        val defaultCaseCode = text("else -> ") +
                                if (construction.hasEmptyDefaultCase())
                                    text("{}")
                                else
                                    nest(nestSize, line() + printConstruction(construction.getDefaultCase(), nestSize))


                        whenCode + keysCode + defaultCaseCode / text("}")
                    }

                    else -> throw IllegalArgumentException("Unknown Construction implementer!")
                }

            val nextConstructionCode = if (construction.hasNextConstruction()) printConstruction(construction.getNextConstruction(), nestSize) else nil()

            return (if (construction !is ElementaryBlock) line() else nil()) + mainCode + breakCode + continueCode + nextConstructionCode
        }
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