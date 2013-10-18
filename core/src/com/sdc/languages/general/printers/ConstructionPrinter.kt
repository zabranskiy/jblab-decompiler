package com.sdc.languages.general.printers

import pretty.*

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


abstract class ConstructionPrinter(expressionPrinter : ExpressionPrinter, statementPrinter : StatementPrinter) {
    val myExpressionPrinter : ExpressionPrinter = expressionPrinter
    val myStatementPrinter : StatementPrinter = statementPrinter

    /***
     * Different types of constructions printing
     */
    open fun printConstruction(construction: Construction?, nestSize: Int): PrimeDoc =
        (if (construction !is ElementaryBlock && construction !is SwitchCase) line() else nil()) + printConstructionOnCurrentLine(construction, nestSize)

    open fun printConstructionOnCurrentLine(construction: Construction?, nestSize: Int): PrimeDoc {
        if (construction == null)
            return nil()
        else {
            val breakCode =
                    if (construction.hasBreak())
                        line() + text("break") + (if (construction.hasBreakToLabel()) text(" " + construction.getBreak()) else nil()) + myStatementPrinter.printStatementsDelimiter()
                    else
                        nil()

            val continueCode =
                    if (construction.hasContinue())
                        line() + text("continue") + (if (construction.hasContinueToLabel()) text(" " + construction.getContinue()) else nil()) + myStatementPrinter.printStatementsDelimiter()
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

            return mainCode + breakCode + continueCode + nextConstructionCode
        }
    }

    open fun printElementaryBlock(elementaryBlock: ElementaryBlock, nestSize: Int): PrimeDoc =
        myStatementPrinter.printStatements(elementaryBlock.getStatements(), nestSize)

    open fun printConditionalBlock(conditionalBlock: ConditionalBlock, nestSize: Int): PrimeDoc {
        val thenPart = printConstruction(conditionalBlock.getThenBlock(), nestSize)

        var elsePart : PrimeDoc = nil()

        if (conditionalBlock.hasElseBlock()) {
            elsePart = text(" else ")
            val elseBlock = conditionalBlock.getElseBlock()

            if (elseBlock is ElementaryBlock && elseBlock.getStatements()!!.isEmpty() && elseBlock.getNextConstruction() is ConditionalBlock) {
                elsePart = elsePart + printConstructionOnCurrentLine(elseBlock.getNextConstruction(), nestSize)
            } else {
                elsePart = elsePart + text("{") + nest(nestSize, printConstruction(elseBlock, nestSize)) / text("}")
            }
        }

        return text("if (") + myExpressionPrinter.printExpression(conditionalBlock.getCondition()?.invert(), nestSize) + text(") {") + nest(nestSize, thenPart) / text("}") + elsePart
    }

    open fun printWhile(whileBlock: While, nestSize: Int): PrimeDoc {
        val body = printConstruction(whileBlock.getBody(), nestSize)

        return text("while (") + myExpressionPrinter.printExpression(whileBlock.getCondition()?.invert(), nestSize) + text(") {") + nest(nestSize, body) / text("}")
    }

    open fun printDoWhile(doWhileBlock: DoWhile, nestSize: Int): PrimeDoc {
        val body = text("do {") + nest(nestSize, printConstruction(doWhileBlock.getBody(), nestSize)) / text("}")

        return body + text(" while (") + myExpressionPrinter.printExpression(doWhileBlock.getCondition(), nestSize) + text(");")
    }

    open fun printFor(forBlock: For, nestSize: Int): PrimeDoc {
        val initialization = myStatementPrinter.printStatement(forBlock.getVariableInitialization(), nestSize)
        val body = printConstruction(forBlock.getBody(), nestSize)
        val afterThought = myStatementPrinter.printStatement(forBlock.getAfterThought(), nestSize)

        return text("for (") + initialization + text("; ") + myExpressionPrinter.printExpression(forBlock.getCondition()?.invert(), nestSize) + text("; ") + afterThought + text(") {") + nest(nestSize, body) / text("}")
    }

    open fun printForEach(forEachBlock: ForEach, nestSize: Int): PrimeDoc {
        val tupleVariables = forEachBlock.getVariables()

        val tuplePrintedVariables = tupleVariables!!.map { arg -> myExpressionPrinter.printExpression(arg, nestSize) }

        var tupleCode : PrimeDoc = tuplePrintedVariables.get(0)

        for (variable in tuplePrintedVariables.drop(1)) {
            tupleCode = tupleCode + text(", ") + variable
        }

        if (tupleVariables.size() > 1) {
            tupleCode = text("(") + tupleCode + text(")")
        }

        val header = text("for (") + tupleCode + text(" ") + printForEachLieInOperator() + text(" ") + myExpressionPrinter.printExpression(forEachBlock.getContainer(), nestSize) + text(") {")
        val body = printConstruction(forEachBlock.getBody(), nestSize)

        return header + nest(nestSize, body) / text("}")
    }

    open fun printTryCatch(tryCatchBlock: TryCatch, nestSize: Int): PrimeDoc {
        val body = printConstruction(tryCatchBlock.getTryBody(), nestSize)

        var catchBlockCode: PrimeDoc = nil()
        for ((variable, catchBody) in tryCatchBlock.getCatches()!!.entrySet()) {
            catchBlockCode = catchBlockCode / text("catch (") + myExpressionPrinter.printExpression(variable, nestSize) + text(") {") + nest(nestSize, printConstruction(catchBody, nestSize)) + text("}")
        }

        val finallyCode =
                if (tryCatchBlock.hasFinallyBlock())
                    text("finally {") + nest(nestSize, printConstruction(tryCatchBlock.getFinallyBody(), nestSize)) / text("}")
                else
                    nil()

        return text("try {") + nest(nestSize, body) / text("}") + catchBlockCode / finallyCode
    }

    open fun printSwitchCase(switchCaseBlock: SwitchCase, nestSize: Int): PrimeDoc {
        var caseKeysCode: PrimeDoc = nil()
        for (key in switchCaseBlock.getKeys()!!.toList()) {
            caseKeysCode = caseKeysCode / text((if (key == null) "default" else "case " + key) + ":")
        }

        return caseKeysCode + nest(nestSize, printConstruction(switchCaseBlock.getCaseBody(), nestSize))
    }


    open fun printSwitch(switchBlock: Switch, nestSize: Int): PrimeDoc {
        var switchCode: PrimeDoc = text("switch (") + myExpressionPrinter.printExpression(switchBlock.getCondition(), nestSize) + text(") {")

        var casesCode: PrimeDoc = nil()
        for (switchCase in switchBlock.getCases()!!.toList()) {
            casesCode = casesCode + nest(nestSize, printConstruction(switchCase, nestSize))
        }

        return switchCode + casesCode / text("}")
    }

    open fun printWhen(whenBlock: When, nestSize: Int): PrimeDoc {
        var whenCode: PrimeDoc = text("when (") + myExpressionPrinter.printExpression(whenBlock.getCondition(), nestSize) + text(") {")

        var keysCode: PrimeDoc = nil()
        for ((key, caseBody) in whenBlock.getCases()!!.entrySet()) {
            keysCode = keysCode / myExpressionPrinter.printExpression(key, nestSize) + text(" -> ") + nest(nestSize, printConstruction(caseBody, nestSize))
        }

        val defaultCaseCode = text("else -> ") +
        if (whenBlock.hasEmptyDefaultCase())
            text("{}")
        else
            nest(nestSize, printConstruction(whenBlock.getDefaultCase(), nestSize))


        return whenCode + nest(nestSize, keysCode / defaultCaseCode) / text("}")
    }


    /***
     * Support stuff
     */
    open fun printForEachLieInOperator(): PrimeDoc = text(":")
}