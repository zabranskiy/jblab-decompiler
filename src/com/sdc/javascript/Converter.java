package com.sdc.javascript;

import com.sdc.ast.expressions.*;
import com.sdc.ast.expressions.identifiers.Variable;
import com.sdc.javascript.expressions.Assignment;
import com.sdc.javascript.expressions.BinaryExpression;
import com.sdc.javascript.expressions.Expression;
import com.sdc.javascript.expressions.Identifier;
import com.sdc.javascript.expressions.Invocation;
import com.sdc.javascript.expressions.UnaryExpression;
import com.sdc.javascript.statements.*;
import com.sdc.javascript.expressions.*;
import com.sdc.javascript.statements.Return;
import com.sdc.javascript.statements.Statement;
import com.sdc.javascript.statements.Throw;

import java.util.ArrayList;
import java.util.List;

public class Converter {
    public static Statement convertStatement(com.sdc.ast.controlflow.Statement statement) {
        if (statement instanceof com.sdc.ast.controlflow.Assignment) {
            com.sdc.ast.controlflow.Assignment st = (com.sdc.ast.controlflow.Assignment) statement;
            return new ExpressionStatement(new Assignment(
                    (Identifier) convertExpression(st.getLeft()),
                    convertExpression(st.getRight())
            ));
        } else if (statement instanceof com.sdc.ast.controlflow.Invocation) {
            com.sdc.ast.controlflow.Invocation st = (com.sdc.ast.controlflow.Invocation) statement;
            List<Expression> args = new ArrayList<Expression>();
            for(com.sdc.ast.expressions.Expression arg : st.getArguments()) {
                args.add(convertExpression(arg));
            }
            return new ExpressionStatement(new Invocation(
                    st.getFunction(),
                    args
            ));
        } else if (statement instanceof com.sdc.ast.controlflow.Return) {
            com.sdc.ast.controlflow.Return st = (com.sdc.ast.controlflow.Return) statement;
            return st.getReturnValue() != null ? new Return(convertExpression(st.getReturnValue())) : new Return();
        } else if (statement instanceof com.sdc.ast.controlflow.Throw) {
            com.sdc.ast.controlflow.Throw st = (com.sdc.ast.controlflow.Throw) statement;
            return new Throw(convertExpression(st.getThrowObject()));
        } else {
            return new BlockStatement(new ArrayList<Statement>());
        }
    }

    public static Expression convertExpression(com.sdc.ast.expressions.Expression expression) {
        if (expression instanceof com.sdc.ast.expressions.identifiers.Field) {
            com.sdc.ast.expressions.identifiers.Field exp = (com.sdc.ast.expressions.identifiers.Field) expression;
            return new Identifier(exp.getName());
        } else if (expression instanceof Variable) {
            Variable exp = (Variable) expression;
            return new Identifier(exp.getName());
        } else if (expression instanceof com.sdc.ast.expressions.BinaryExpression) {
            com.sdc.ast.expressions.BinaryExpression exp = (com.sdc.ast.expressions.BinaryExpression) expression;
            BinaryExpression.OperationType op;
            switch (exp.getOperationType()) {
                case ADD:
                    op = BinaryExpression.OperationType.ADD;
                    break;
                case SUB:
                    op = BinaryExpression.OperationType.SUB;
                    break;
                case DIV:
                    op = BinaryExpression.OperationType.DIV;
                    break;
                case MUL:
                    op = BinaryExpression.OperationType.MUL;
                    break;
                case AND:
                    op = BinaryExpression.OperationType.AND;
                    break;
                case OR:
                    op = BinaryExpression.OperationType.OR;
                    break;
                case EQUALITY:
                    op = BinaryExpression.OperationType.EQUALS;
                    break;
                case INEQUALITY:
                    op = BinaryExpression.OperationType.NEQUALS;
                    break;
                case GE:
                    op = BinaryExpression.OperationType.GE;
                    break;
                case GREATER:
                    op = BinaryExpression.OperationType.GREATER;
                    break;
                case LE:
                    op = BinaryExpression.OperationType.LE;
                    break;
                case LESS:
                    op = BinaryExpression.OperationType.LESS;
                    break;
                default:
                    op = BinaryExpression.OperationType.ADD;
                    break;
            }
            return new BinaryExpression(op, convertExpression(exp.getLeft()), convertExpression(exp.getRight()));
        } else if (expression instanceof Constant) {
            Constant exp = (Constant) expression;
            return new Literal(exp.getValue());
        } else if (expression instanceof com.sdc.ast.expressions.Invocation) {
            com.sdc.ast.expressions.Invocation exp = (com.sdc.ast.expressions.Invocation) expression;
            List<Expression> args = new ArrayList<Expression>();
            for(com.sdc.ast.expressions.Expression arg : exp.getArguments()) {
                args.add(convertExpression(arg));
            }
            return new Invocation(
                    exp.getFunction(),
                    args
            );
        } else if (expression instanceof com.sdc.ast.expressions.UnaryExpression) {
            com.sdc.ast.expressions.UnaryExpression exp = (com.sdc.ast.expressions.UnaryExpression) expression;
            UnaryExpression.OperationType op;
            switch (exp.getType()) {
                case NOT:
                    op = UnaryExpression.OperationType.NOT;
                    break;
                case NEGATE:
                    op = UnaryExpression.OperationType.NEGATE;
                    break;
                default:
                    op = UnaryExpression.OperationType.NOT;
                    break;
            }
            return new UnaryExpression(op, convertExpression(exp.getOperand()));
        } else {
            return new Literal(null);
        }
    }
}
