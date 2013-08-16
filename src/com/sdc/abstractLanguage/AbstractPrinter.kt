package com.sdc.abstractLanguage

import pretty.*
import com.sdc.ast.OperationType
import com.sdc.ast.expressions.Expression
import com.sdc.ast.expressions.Constant
import com.sdc.ast.expressions.BinaryExpression
import com.sdc.ast.expressions.UnaryExpression
import com.sdc.ast.expressions.TernaryExpression
import com.sdc.ast.expressions.identifiers.Field
import com.sdc.ast.expressions.identifiers.Variable
import com.sdc.ast.expressions.NewArray
import com.sdc.ast.expressions.nestedclasses.AnonymousClass
import com.sdc.ast.expressions.ExprIncrement
import com.sdc.ast.expressions.InstanceOf
import com.sdc.ast.expressions.PriorityExpression
import com.sdc.ast.controlflow.Statement
import com.sdc.ast.controlflow.Assignment
import com.sdc.ast.controlflow.Return
import com.sdc.ast.controlflow.Throw
import com.sdc.ast.controlflow.ExpressionWrapper
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
import com.sdc.cfg.constructions.SwitchCase
import com.sdc.ast.expressions.ArrayLength

abstract class AbstractPrinter {
    abstract fun getOperationPrinter(): AbstractOperationPrinter;

    open fun printExpression(expression: Expression?, nestSize: Int): PrimeDoc =
        when (expression) {
            is Constant -> printConstant(expression,nestSize)
            is BinaryExpression -> printBinaryExpression(expression,nestSize)
            is UnaryExpression -> printUnaryExpression(expression,nestSize)
            is Field -> printField(expression,nestSize)
            is Variable -> printVariable(expression,nestSize)
            is com.sdc.ast.expressions.Invocation -> printInvocationExpression(expression,nestSize)
            is com.sdc.ast.expressions.New -> printNew(expression, nestSize)
            is NewArray -> printNewArray(expression,nestSize)
            is InstanceOf -> printInstanceOf(expression, nestSize)
            is AnonymousClass -> printAnonymousClassExpression(expression, nestSize)
            is TernaryExpression -> printTernaryExpression(expression,nestSize)
            is ExprIncrement -> printExprIncrement(expression,nestSize)
            is ArrayLength -> printArrayLength(expression,nestSize)
            else -> throw IllegalArgumentException("Unknown Expression implementer!")
        }

    open fun printExpressionCheckBrackets(expression: Expression?, nextOpPriority: Int, nestSize: Int): PrimeDoc =
            printExpressionCheckBrackets(expression, nextOpPriority, false, nestSize)

    open fun printExpressionCheckBrackets(expression: Expression?, nextOpPriority: Int, nextOpAssociative: Boolean, nestSize: Int): PrimeDoc {
        if (expression is PriorityExpression) {
            val priority = expression.getPriority(getOperationPrinter())
            if (nextOpPriority < priority || (nextOpPriority == priority && !nextOpAssociative ) )
                return printExpressionWithBrackets(expression, nestSize)

        }
        return printExpression(expression, nestSize);
    }

    open fun printStatement(statement: Statement?, nestSize: Int): PrimeDoc =
        when (statement) {
            is ExpressionWrapper -> printExpression(statement.toExpression(), nestSize)
            is Assignment -> printAssignment(statement, nestSize)
            is Return -> printReturn(statement, nestSize)
            is Throw -> printThrow(statement, nestSize)
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
                    is ElementaryBlock -> printElementaryBlock(construction, nestSize)
                    is ConditionalBlock -> printConditionalBlock(construction, nestSize)
                    is While -> printWhile(construction, nestSize)
                    is DoWhile -> printDoWhile(construction, nestSize)
                    is For -> printFor(construction, nestSize)
                    is ForEach -> printForEach(construction, nestSize)
                    is TryCatch -> printTryCatch(construction, nestSize)
                    is SwitchCase -> printSwitchCase(construction, nestSize)
                    is Switch -> printSwitch(construction, nestSize)
                    is When -> printWhen(construction, nestSize)
                    else -> throw IllegalArgumentException("Unknown Construction implementer!")
                }

            val nextConstructionCode = if (construction.hasNextConstruction()) printConstruction(construction.getNextConstruction(), nestSize) else nil()

            return (if (construction !is ElementaryBlock && construction !is SwitchCase) line() else nil()) + mainCode + breakCode + continueCode + nextConstructionCode
        }
    }

    open fun printConstant(expression : Constant, nestSize : Int): PrimeDoc =
        if (!expression.isStringValue())
            text(expression.getValue().toString())
        else
            text("\"" + expression.getValue().toString() + "\"")

    open fun printBinaryExpression(expression : BinaryExpression, nestSize : Int): PrimeDoc  {
        val l = expression.getLeft()
        val r = expression.getRight()
        val opPriority = expression.getPriority(getOperationPrinter())
        val left = printExpressionCheckBrackets(l,opPriority,nestSize);
        val right = printExpressionCheckBrackets(r,opPriority,expression.isAssociative(),nestSize);
        return group(left / (text(expression.getOperation(getOperationPrinter())) + right))
    }

    open fun printUnaryExpression(expression : UnaryExpression, nestSize : Int): PrimeDoc {
        val opPriority = expression.getPriority(getOperationPrinter())
        val operand = expression.getOperand()
        val expr = printExpressionCheckBrackets(operand,opPriority,nestSize);
        return text(expression.getOperation(getOperationPrinter())) + expr
    }


    open fun printField(expression : Field, nestSize : Int): PrimeDoc {
        val owner = expression.getOwner()
        val ownerName = if (owner != null) printInstance(owner, nestSize) else text(expression.getStaticOwnerName() + ".")
        return ownerName + text(expression.getName())
    }

    open fun printVariable(expression : Variable, nestSize : Int): PrimeDoc {
        if (expression.getArrayIndex() == null)
            return text(printVariableName(expression.getName()))
        else {
            return group(printExpression(expression.getArrayVariable(), nestSize)
                    + text("[") + printExpression(expression.getArrayIndex(), nestSize) + text("]")
            )
        }
    }

    open fun printInvocationExpression(expression : com.sdc.ast.expressions.Invocation, nestSize : Int): PrimeDoc {
        var funName : PrimeDoc = group(text(expression.getFunction()))
        if (expression is com.sdc.ast.expressions.InstanceInvocation) {
            funName = printInstance(expression.getInstance(), nestSize) + funName
        }
        return funName + printInvocationArguments(expression.getArguments(), nestSize)
    }

    open fun printNew(expression : com.sdc.ast.expressions.New, nestSize : Int): PrimeDoc =
            printNewOperator() + printExpression(expression.getConstructor(), nestSize)

    open fun printNewArray(expression : NewArray, nestSize : Int): PrimeDoc {
        var newArray = group(text("new") + nest(nestSize, line() + text(expression.getType())))
        for (dimension in expression.getDimensions()!!.toList()) {
            newArray = group(newArray + text("[") + printExpression(dimension, nestSize) + text("]"))
        }
        return newArray
    }

    open fun printInstanceOf(expression : InstanceOf, nestSize : Int): PrimeDoc =
            printExpression(expression.getArgument(), nestSize) + printInstanceOfOperator() + text(expression.getType())

    open fun printAnonymousClassExpression(expression : AnonymousClass, nestSize : Int): PrimeDoc =
            printNewOperator() + printAnonymousClass(expression.getNestedClass(), expression.getConstructorArguments())

    open fun printTernaryExpression(expression : TernaryExpression, nestSize : Int): PrimeDoc {
        val l = expression.getLeft()
        val r = expression.getRight()
        var c=  expression.getCondition();
        val opPriority = expression.getPriority(getOperationPrinter())
        val condition = printExpressionCheckBrackets(c,opPriority,nestSize);
        val left = printExpressionCheckBrackets(l,opPriority,nestSize);
        val right = printExpressionCheckBrackets(r,opPriority,nestSize);
        return group(nest(nestSize, line() + text("(") + condition+text(") ? ") + left + text(" : ") + right))
    }

    open fun printExprIncrement(expression : ExprIncrement, nestSize : Int): PrimeDoc {
        val expr = expression.getVariable();
        val printExpr = printExpression(expr, nestSize)
        var myType = expression.getOperationType();
        var operation = expression.getOperation(getOperationPrinter());
        if(myType == OperationType.INC_REV || myType == OperationType.DEC_REV){
            return group(nest(nestSize, text(operation) + printExpr))
        }   else{
            val increment = expression.getIncrementExpression();
            val priority = expression.getPriority(getOperationPrinter())
            val printIncrement =
                    if(increment is Constant){
                        printExpression(increment, nestSize)
                    } else{
                        printExpressionCheckBrackets(increment, priority, nestSize)
                    }
            if(expression.IsIncrementSimple()){
                return group(nest(nestSize, printExpr + text(operation)))
            } else{
                return group(nest(nestSize, printExpr / ( text(operation) + printIncrement)))
            }
        }
    }

    open fun printArrayLength(expression : ArrayLength, nestSize : Int): PrimeDoc {
        val expr = expression.getOperand();
        val priority=expression.getPriority(getOperationPrinter())
        val printOperand = printExpressionCheckBrackets(expr,priority, nestSize)
        return group(nest(nestSize, printOperand + text(expression.getOperation(getOperationPrinter()))))
    }

    open fun printAssignment(statement : Assignment, nestSize : Int): PrimeDoc =
            printExpression(statement.getLeft(), nestSize) + text(" = ") + printExpression(statement.getRight(), nestSize)

    open fun printReturn(statement : Return, nestSize : Int): PrimeDoc {
        var returnStatement : PrimeDoc = if (statement.needToPrintReturn()) text("return") else text("")
        if (statement.getReturnValue() != null)
            returnStatement = returnStatement + text(" ") + printExpression(statement.getReturnValue(), nestSize)
        return returnStatement
    }

    open fun printThrow(statement : Throw, nestSize : Int): PrimeDoc =
            group(text("throw") + nest(nestSize, line()
            + printExpression(statement.getThrowObject(), nestSize)))

    open fun printElementaryBlock(elementaryBlock : ElementaryBlock, nestSize : Int): PrimeDoc =
        printStatements(elementaryBlock.getStatements(), nestSize)

    open fun printConditionalBlock(conditionalBlock : ConditionalBlock, nestSize : Int): PrimeDoc {
        val thenPart = printConstruction(conditionalBlock.getThenBlock(), nestSize)

        var elsePart : PrimeDoc = nil()
        if (conditionalBlock.hasElseBlock()) {
            elsePart = text(" else {") + nest(nestSize, printConstruction(conditionalBlock.getElseBlock(), nestSize)) / text("}")
        }

        return text("if (") + printExpression(conditionalBlock.getCondition()?.invert(), nestSize) + text(") {") + nest(nestSize, thenPart) / text("}") + elsePart
    }

    open fun printWhile(whileBlock : While, nestSize : Int): PrimeDoc {
        val body = printConstruction(whileBlock.getBody(), nestSize)
        return text("while (") + printExpression(whileBlock.getCondition()?.invert(), nestSize) + text(") {") + nest(nestSize, body) / text("}")
    }

    open fun printDoWhile(doWhileBlock : DoWhile, nestSize : Int): PrimeDoc {
        val body = text("do {") + nest(nestSize, printConstruction(doWhileBlock.getBody(), nestSize)) / text("}")
        return body + text(" while (") + printExpression(doWhileBlock.getCondition(), nestSize) + text(")")
    }

    open fun printFor(forBlock : For, nestSize : Int): PrimeDoc {
        val body = printConstruction(forBlock.getBody(), nestSize)
        val initialization = printStatement(forBlock.getVariableInitialization(), nestSize)
        val afterThought = printStatement(forBlock.getAfterThought(), nestSize)
        return text("for (") + initialization + text(", ") + printExpression(forBlock.getCondition()?.invert(), nestSize) + text(", ") + afterThought + text(") {") + nest(nestSize, body) / text("}")
    }

    open fun printForEach(forEachBlock : ForEach, nestSize : Int): PrimeDoc {
        val body = printConstruction(forEachBlock.getBody(), nestSize)
        return text("for (") + printExpression(forEachBlock.getVariable(), nestSize) + text(" : ") + printExpression(forEachBlock.getContainer(), nestSize) + text(") {") + nest(nestSize, body) / text("}")
    }

    open fun printTryCatch(tryCatchBlock : TryCatch, nestSize : Int): PrimeDoc {
        val body = printConstruction(tryCatchBlock.getTryBody(), nestSize)

        var catchBlockCode : PrimeDoc = nil()
        for ((variable, catchBody) in tryCatchBlock.getCatches()!!.entrySet()) {
            catchBlockCode = catchBlockCode / text("catch (") + printExpression(variable, nestSize) + text(") {") + nest(nestSize, printConstruction(catchBody, nestSize)) + text("}")
        }

        val finallyCode =
                if (tryCatchBlock.hasFinallyBlock())
                    text("finally {") + nest(nestSize, printConstruction(tryCatchBlock.getFinallyBody(), nestSize)) / text("}")
                else
                    nil()

        return text("try {") + nest(nestSize, body) / text("}") + catchBlockCode / finallyCode
    }

    open fun printSwitchCase(switchCaseBlock : SwitchCase, nestSize : Int): PrimeDoc {
        var caseKeysCode : PrimeDoc = nil()
        for (key in switchCaseBlock.getKeys()!!.toArray()) {
            caseKeysCode = caseKeysCode / text((if (key == null) "default" else "case " + key) + ":")
        }

        return caseKeysCode + nest(nestSize, printConstruction(switchCaseBlock.getCaseBody(), nestSize))
    }


    open fun printSwitch(switchBlock : Switch, nestSize : Int): PrimeDoc {
        var switchCode : PrimeDoc =  text("switch (") + printExpression(switchBlock.getCondition(), nestSize) + text(") {")

        var casesCode : PrimeDoc = nil()
        for (switchCase in switchBlock.getCases()!!.toList()) {
            casesCode = casesCode + nest(nestSize, printConstruction(switchCase, nestSize))
        }

        return switchCode + casesCode / text("}")
    }

    open fun printWhen(whenBlock : When, nestSize : Int): PrimeDoc {
        var whenCode : PrimeDoc = text("when (") + printExpression(whenBlock.getCondition(), nestSize) + text(") {")

        var keysCode : PrimeDoc = nil()
        for ((key, caseBody) in whenBlock.getCases()!!.entrySet()) {
            keysCode = keysCode / printExpression(key, nestSize) + text(" -> ") + nest(nestSize, line() + printConstruction(caseBody, nestSize))
        }

        val defaultCaseCode = text("else -> ") +
        if (whenBlock.hasEmptyDefaultCase())
            text("{}")
        else
            nest(nestSize, line() + printConstruction(whenBlock.getDefaultCase(), nestSize))


        return whenCode + keysCode + defaultCaseCode / text("}")
    }

    open fun printInstance(instance : Expression?, nestSize : Int): PrimeDoc {
        var instanceName : PrimeDoc = nil()
        if (instance is Variable) {
            var variableName = instance.getName()
            if (!variableName.equals("this")) {
                variableName =  printVariableName(variableName)
                instanceName = text(variableName) + text(".")
            }
        } else {
            instanceName = printExpression(instance, nestSize) + text(".")
        }
        return instanceName
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

    open fun printExpressionWithBrackets(expression: Expression?, nestSize: Int): PrimeDoc =
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