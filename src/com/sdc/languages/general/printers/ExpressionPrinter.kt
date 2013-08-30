package com.sdc.languages.general.printers

import pretty.*
import com.sdc.ast.Type
import com.sdc.ast.ExpressionType
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
import com.sdc.ast.expressions.ArrayLength
import com.sdc.ast.expressions.SquareBrackets
import com.sdc.ast.expressions.Cast
import com.sdc.ast.expressions.Invocation
import com.sdc.ast.expressions.New
import com.sdc.languages.general.languageParts.GeneralClass
import com.sdc.ast.expressions.InstanceInvocation

abstract class ExpressionPrinter(printer : Printer) {
    val myPrinter : Printer = printer

    abstract fun getOperationPrinter(): OperationPrinter;

    /***
     * Different types of expressions printing
     */
    open fun printExpression(expression: Expression?, nestSize: Int): PrimeDoc =
        when (expression) {
            is Constant -> printConstant(expression, nestSize)
            is BinaryExpression -> printBinaryExpression(expression, nestSize)
            is UnaryExpression -> printUnaryExpression(expression, nestSize)
            is Field -> printField(expression, nestSize)
            is Variable -> printVariable(expression, nestSize)
            is Invocation -> printInvocation(expression, nestSize)
            is New -> printNew(expression, nestSize)
            is NewArray -> printNewArray(expression, nestSize)
            is InstanceOf -> printInstanceOf(expression, nestSize)
            is AnonymousClass -> printAnonymousClassExpression(expression, nestSize)
            is TernaryExpression -> printTernaryExpression(expression, nestSize)
            is ExprIncrement -> printExprIncrement(expression, nestSize)
            is ArrayLength -> printArrayLength(expression, nestSize)
            is SquareBrackets -> printSquareBrackets(expression, nestSize)
            is Cast -> printCast(expression, nestSize)
            else -> throw IllegalArgumentException("Unknown Expression implementer!")
        }

    open fun printConstant(expression: Constant, nestSize: Int): PrimeDoc =
        if (!expression.isStringValue())
            text(expression.getValue().toString())
        else
            text("\"" + expression.getValue().toString() + "\"")

    open fun printBinaryExpression(expression: BinaryExpression, nestSize: Int): PrimeDoc {
        val l = expression.getLeft()
        val r = expression.getRight()

        val isAssociative = expression.isAssociative();
        val opPriority = expression.getPriority(getOperationPrinter())

        val left = printExpressionCheckBrackets(l, opPriority, isAssociative, nestSize);
        val right = printExpressionCheckBrackets(r, opPriority, isAssociative, nestSize);

        return group(left / (text(expression.getOperation(getOperationPrinter())) + right))
    }

    open fun printUnaryExpression(expression: UnaryExpression, nestSize: Int): PrimeDoc {
        val opPriority = expression.getPriority(getOperationPrinter())
        val isAssociative = expression.isAssociative();
        val operand = expression.getOperand()
        val expr = printExpressionCheckBrackets(operand, opPriority, isAssociative, nestSize);

        return text(expression.getOperation(getOperationPrinter())) + expr
    }

    open fun printCast(expression: Cast, nestSize: Int): PrimeDoc {
        val opPriority = expression.getPriority(getOperationPrinter())
        val isAssociative = expression.isAssociative();
        val operand = expression.getOperand()
        val expr = printExpressionCheckBrackets(operand, opPriority, isAssociative, nestSize);

        return text(expression.getOperation(getOperationPrinter())) + expr
    }

    open fun printField(expression: Field, nestSize: Int): PrimeDoc {
        val owner = expression.getOwner()

        var ownerName = if (owner == null) text(expression.getStaticOwnerName() + ".") else nil()

        if (owner != null && ((expression.getName() as Constant).getValue().toString().startsWith("this$")
            || !(owner is Field && (owner.getName() as Constant).getValue().toString().startsWith("this$"))))
        {
            ownerName = printInstance(owner, nestSize)
        }

        return ownerName + printExpression(expression.getName(), nestSize)
    }

    open fun printVariable(expression: Variable, nestSize: Int): PrimeDoc =
        if (expression.isDeclared())
            printExpression(expression.getName(), nestSize)
        else {
            val result = printUndeclaredVariable(expression, nestSize)
            expression.declare()
            result
        }

    open fun printInvocation(expression: Invocation, nestSize: Int): PrimeDoc {
        var funName: PrimeDoc = group(text(expression.getFunction()))
        val opPriority = expression.getPriority(getOperationPrinter());
        val isAssociative = expression.isAssociative();

        if (expression is InstanceInvocation) {
            val appendExpressions : MutableList<Expression> = expression.getAppendSequenceExpressions() as MutableList<Expression>
            if (appendExpressions.isEmpty()) {
                val instance = expression.getInstance()
                if (!(instance is Field && (instance.getName() as Constant).getValue().toString().startsWith("this$"))) {
                    funName = printInstance(instance, opPriority, isAssociative, nestSize, expression.isNotNullCheckedCall()) + funName
                }
            } else {
                var lastSumExpression = appendExpressions.remove(appendExpressions.size() - 1)
                funName = nil();
                for (sumExpression in appendExpressions) {
                    funName = nest(nestSize, funName + printExpression(sumExpression, nestSize) + text(" + "))
                }
                funName = group(funName + printExpression(lastSumExpression, nestSize))
                return funName
            }
        }

        return funName + printInvocationArguments(expression.getArguments(), nestSize)
    }

    open fun printNew(expression: New, nestSize: Int): PrimeDoc =
        printNewOperator() + printExpression(expression.getConstructor(), nestSize)

    open fun printNewArray(expression: NewArray, nestSize: Int): PrimeDoc {
        var newArray = group(nil());

        if (expression.hasInitialization()) {
            newArray = group(newArray + text("{"))

            val values = expression.getInitializationValues()
            val lastValue = values!!.remove(values.size() - 1)

            for (value in values) {
                newArray = group(newArray + printExpression(value, nestSize) + text(", "))
            }

            newArray = group(newArray + printExpression(lastValue, nestSize) + text("}"))
        } else {
            newArray = group(text("new") + nest(nestSize, line() + printType(expression.getType(), nestSize)))

            for (dimension in expression.getDimensions()!!.toList()) {
                newArray = group(newArray + text("[") + printExpression(dimension, nestSize) + text("]"))
            }
        }

        return newArray
    }

    open fun printInstanceOf(expression : InstanceOf, nestSize : Int): PrimeDoc {
        if (!expression.isInverted()) {
            return printInstanceOfArgument(expression, nestSize) + printInstanceOfOperator() + printType(expression.getType(), nestSize)
        } else {
            return printInvertedInstanceOf(expression, nestSize)
        }
    }

    open fun printAnonymousClassExpression(expression: AnonymousClass, nestSize: Int): PrimeDoc =
        printNewOperator() + printAnonymousClass(expression.getNestedClass(), expression.getConstructorArguments())

    open fun printTernaryExpression(expression: TernaryExpression, nestSize: Int): PrimeDoc {
        val l = expression.getLeft()
        val r = expression.getRight()
        var c = expression.getCondition();

        val opPriority = expression.getPriority(getOperationPrinter())
        val condition = printExpressionCheckBrackets(c, opPriority, nestSize);

        val left = printExpressionCheckBrackets(l, opPriority, nestSize);
        val right = printExpressionCheckBrackets(r, opPriority, nestSize);

        return group(nest(nestSize, line() + text("(") + condition + text(") ? ") + left + text(" : ") + right))
    }

    open fun printExprIncrement(expression: ExprIncrement, nestSize: Int): PrimeDoc {
        val expr = expression.getVariable();
        val printExpr = printExpression(expr, nestSize)

        var myType = expression.getExpressionType();
        var operation = expression.getOperation(getOperationPrinter());

        if (myType == ExpressionType.INC_REV || myType == ExpressionType.DEC_REV) {
            return group(nest(nestSize, text(operation) + printExpr))
        } else {
            val increment = expression.getIncrementExpression();
            val priority = expression.getPriority(getOperationPrinter())
            val isAssociative = expression.isAssociative();

            val printIncrement =
                    if (increment is Constant) {
                        printExpression(increment, nestSize)
                    } else {
                        printExpressionCheckBrackets(increment, priority, isAssociative, nestSize)
                    }

            if (expression.IsIncrementSimple()) {
                return group(nest(nestSize, printExpr + text(operation)))
            } else {
                return group(nest(nestSize, printExpr / (text(operation) + printIncrement)))
            }
        }
    }

    open fun printArrayLength(expression: ArrayLength, nestSize: Int): PrimeDoc {
        val expr = expression.getOperand();
        val priority = expression.getPriority(getOperationPrinter())
        val isAssociative = expression.isAssociative();
        val printOperand = printExpressionCheckBrackets(expr, priority,isAssociative, nestSize)

        return group(nest(nestSize, printOperand + text(expression.getOperation(getOperationPrinter()))))
    }

    open fun printSquareBrackets(expression: SquareBrackets, nestSize: Int): PrimeDoc {
        val operand = expression.getOperand()
        val index = expression.getIndex()

        val priority = expression.getPriority(getOperationPrinter())
        val isAssociative = expression.isAssociative();

        val printOperand = printExpressionCheckBrackets(operand, priority, isAssociative, nestSize)
        val printIndex = printExpression(index, nestSize)

        return group(nest(nestSize, printOperand + text("[") + printIndex + text("]")))
    }


    /***
     * Support stuff
     */
    open fun printInstanceOfOperator(): PrimeDoc = text("instanceof ")

    open fun printNewOperator(): PrimeDoc = text("new ")

    open fun printVariableName(variableName: String?): String? = variableName

    open fun printUndeclaredVariable(expression: Variable, nestSize: Int): PrimeDoc =
        printType(expression.getType(), nestSize) + printExpression(expression.getName(), nestSize)


    open fun printExpressionWithBrackets(expression: Expression?, nestSize: Int): PrimeDoc =
        text("(") + printExpression(expression, nestSize) + text(")")

    open fun printExpressionCheckBrackets(expression: Expression?, nextOpPriority: Int, nestSize: Int): PrimeDoc =
            printExpressionCheckBrackets(expression, nextOpPriority, false, nestSize)

    open fun printExpressionCheckBrackets(expression: Expression?, nextOpPriority: Int, nextOpAssociative: Boolean, nestSize: Int): PrimeDoc {
        if (expression is PriorityExpression) {
            val priority = expression.getPriority(getOperationPrinter())
            if (nextOpPriority < priority || (nextOpPriority == priority && !nextOpAssociative))
                return printExpressionWithBrackets(expression, nestSize)
        }

        return printExpression(expression, nestSize);
    }

    open fun printType(myType: Type?, nestSize: Int): PrimeDoc =
        text(myType?.toString(getOperationPrinter()))

    open fun printInstance(instance: Expression?, nestSize: Int, isNotNullCheckedCall : Boolean = false): PrimeDoc {
        var instanceName: PrimeDoc = nil()
        if (instance is Variable ) {
            if (!instance.isThis()) {
                instanceName = printExpression(instance, nestSize) + text(if (isNotNullCheckedCall) "!!." else ".")
            }
        } else {
            instanceName = printExpression(instance, nestSize) + text(if (isNotNullCheckedCall) "!!." else ".")
        }
        return instanceName
    }

    open fun printInstance(instance: Expression?, opPriority: Int, isAssociative: Boolean, nestSize: Int, isNotNullCheckedCall : Boolean = false): PrimeDoc {
        var instanceName: PrimeDoc = nil()
        if (instance is Variable ) {
            if (!instance.isThis()) {
                instanceName = printExpressionCheckBrackets(instance, opPriority, isAssociative, nestSize) + text(if (isNotNullCheckedCall) "!!." else ".")
            }
        } else {
            instanceName = printExpressionCheckBrackets(instance, opPriority, isAssociative, nestSize) + text(if (isNotNullCheckedCall) "!!." else ".")
        }
        return instanceName
    }

    open fun printInvocationArguments(arguments: List<Expression>?, nestSize: Int): PrimeDoc =
        if (arguments!!.isEmpty())
            text("()")
        else {
            var argsDocs = arguments.take(arguments.size - 1)
                    .map { arg -> printExpression(arg, nestSize) + text(", ") }
            var printLastArgument : PrimeDoc = nil();

            val lastArgument = arguments.last

            if (lastArgument is NewArray && lastArgument.hasInitialization()) {
                printLastArgument = printNewArrayWithInitializationAsSequence(lastArgument, nestSize)
            }  else {
                printLastArgument = printExpression(lastArgument, nestSize)
            }

            val argumentsCode: PrimeDoc = nest(2 * nestSize, fill(argsDocs + printLastArgument))

            text("(") + argumentsCode + text(")")
        }

    open fun printNewArrayWithInitializationAsSequence(expression: NewArray, nestSize: Int): PrimeDoc {
        var newArray = group(nil());

        if (expression.hasInitialization()) {
            val values = expression.getInitializationValues()
            val lastValue = values!!.remove(values.size()-1)

            for (value in values){
                newArray = group(newArray + printExpression(value, nestSize) + text(", "))
            }

            newArray = group(newArray + printExpression(lastValue, nestSize))
        }

        return newArray
    }

    open fun printInstanceOfArgument(expression : InstanceOf, nestSize : Int): PrimeDoc {
        val hasArgument = expression.getArgument() != null

        var argument : PrimeDoc = nil()
        if (hasArgument) {
            argument = printExpression(expression.getArgument(), nestSize) + text(" ")
        }

        return argument
    }

    open fun printInvertedInstanceOf(expression : InstanceOf, nestSize : Int): PrimeDoc =
        printExpression(UnaryExpression(ExpressionType.NOT, expression.invert()), nestSize)

    open fun printAnonymousClassDeclaration(anonymousClass: GeneralClass?, arguments: List<Expression>?): PrimeDoc =
        if (anonymousClass!!.getSuperClass()!!.isEmpty()) {
            val implementedInterfaces = anonymousClass.getImplementedInterfaces()
            val declaration =
                    if (implementedInterfaces!!.isEmpty())
                        myPrinter.printBaseClass()
                    else
                        text(implementedInterfaces.get(0))
            declaration + text("() {")
        } else {
            text(anonymousClass.getSuperClass()) + printInvocationArguments(arguments, anonymousClass.getNestSize())
        }

    open fun printAnonymousClass(anonymousClass: GeneralClass?, arguments: List<Expression>?): PrimeDoc {
        val declaration = printAnonymousClassDeclaration(anonymousClass, arguments)

        var anonClassCode : PrimeDoc = declaration + nest(anonymousClass!!.getNestSize(), myPrinter.printClassBodyInnerClasses(anonymousClass))

        for (classField in anonymousClass.getFields()!!.toList())
            anonClassCode = anonClassCode + nest(anonymousClass.getNestSize(), line() + myPrinter.printField(classField))

        for (classMethod in anonymousClass.getMethods()!!.toList())
            anonClassCode = anonClassCode + nest(anonymousClass.getNestSize(), line() + myPrinter.printMethod(classMethod))

        return anonClassCode / text("}")
    }
}
