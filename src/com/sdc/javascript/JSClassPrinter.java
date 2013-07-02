package com.sdc.javascript;

import com.sdc.javascript.expressions.*;
import com.sdc.javascript.statements.*;
import java.util.ArrayList;
import java.util.List;

// REWRITE IT COMPLETELY!!!
public class JSClassPrinter {
    public static String printJSClass(JSClass jsClass) {
        List<Expression> args = new ArrayList<Expression>();
        args.add(new Identifier(jsClass.getName()));
        List<Property> properties = new ArrayList<Property>();
        for(JSClassField identifier : jsClass.getFields()) {
            properties.add(new Property(
                    new Identifier(identifier.getName()),
                    identifier.getDefaultValue()
            ));
        }
        for(JSClassMethod method : jsClass.getMethods()) {
            FunctionExpression fun = new FunctionExpression(
                    method.getParameters(),
                    new BlockStatement(method.getBody())
            );
            properties.add(new Property(
                    new Identifier(method.getName()),
                    fun));
        }
        args.add(new ObjectExpression(properties));
        return printStatement(new ExpressionStatement(new Invocation(
                "classes.Class",
                args
        )));
    }

    public static String printExpression(Expression expression) {
        if (expression instanceof Assignment) {
            Assignment exp = (Assignment) expression;
            return exp.left.getName() + " = " + printExpression(exp.right);
        } else if (expression instanceof BinaryExpression) {
            BinaryExpression exp = (BinaryExpression) expression;
            String op = "";
            switch (exp.type) {
                case ADD:
                    op = "+";
                    break;
                case SUB:
                    op = "-";
                    break;
                case MUL:
                    op = "*";
                    break;
                case DIV:
                    op = "/";
                    break;
                case MOD:
                    op = "%";
                    break;
                case LSHIFT:
                    op = "<<";
                    break;
                case SRSHIFT:
                    op = ">>";
                    break;
                case URSHIFT:
                    op = ">>>";
                    break;
                case LESS:
                    op = "<";
                    break;
                case LE:
                    op = "<=";
                    break;
                case GREATER:
                    op = ">";
                    break;
                case GE:
                    op = ">=";
                    break;
                case EQUALS:
                    op = "==";
                    break;
                case NEQUALS:
                    op = "!=";
                    break;
                case SEQUALS:
                    op = "===";
                    break;
                case SNEQUALS:
                    op = "!===";
                    break;
                case AND:
                    op = "&&";
                    break;
                case OR:
                    op = "||";
                    break;
                case BITAND:
                    op = "&";
                    break;
                case BITXOR:
                    op = "^";
                    break;
                case BITOR:
                    op = "|";
                    break;
            }
            op = " " + op + " ";
            return "(" + printExpression(exp.left) + ")" + op + "(" + printExpression(exp.right) + ")";
        } else if (expression instanceof ConditionalExpression) {
            ConditionalExpression exp = (ConditionalExpression) expression;
            return printExpression(exp.test) + " ? " + printExpression(exp.consequent) + " : " +
                    printExpression(exp.alternate);
        } else if (expression instanceof FunctionExpression) {
            FunctionExpression exp = (FunctionExpression) expression;
            StringBuilder fun = new StringBuilder("function (");
            boolean c = false;
            for (String name : exp.params) {
                fun.append((c ? ", " : "") + name);
                c = true;
            }
            fun.append(") ");
            fun.append(printStatement(exp.body));
            return fun.toString();
        } else if (expression instanceof Identifier) {
            Identifier exp = (Identifier) expression;
            return exp.getName();
        } else if (expression instanceof Invocation) {
            Invocation exp = (Invocation) expression;
            StringBuilder fun = new StringBuilder(exp.function + "(");
            boolean c = false;
            for (Expression e : exp.arguments) {
                fun.append((c ? ", " : "") + printExpression(e));
                c = true;
            }
            fun.append(")");
            return fun.toString();
        } else if (expression instanceof ObjectExpression) {
            ObjectExpression exp = (ObjectExpression) expression;
            StringBuilder obj = new StringBuilder("{\n");
            boolean c = false;
            for (Property p : exp.properties) {
                obj.append((c ? ",\n" : "") + printProperty(p));
                c = true;
            }
            obj.append("}");
            return obj.toString();
        } else if (expression instanceof UnaryExpression) {
            UnaryExpression exp = (UnaryExpression) expression;
            String op = "";
            switch (exp.type) {
                case NOT:
                    op = "!";
                    break;
                case NEGATE:
                    op = "-";
                    break;
                case INC:
                    op = "++";
                    break;
                case DEC:
                    op = "--";
                    break;
            }
            return op + printExpression(exp.operand);
        } else {
            return "";
        }
    }

    private static String printProperty(Property property) {
        return property.key + " : " + printExpression(property.value);
    }

    public static String printStatement(Statement statement) {
        if (statement instanceof BlockStatement) {
            BlockStatement st = (BlockStatement) statement;
            StringBuilder body = new StringBuilder("{\n");
            for (Statement s : st.body) {
                body.append(printStatement(s));
            }
            body.append("}\n");
            return body.toString();
        } else if (statement instanceof BreakStatement) {
            return "break;\n";
        } else if (statement instanceof ContinueStatement) {
            return "continue;\n";
        } else if (statement instanceof DoWhileStatement) {
            DoWhileStatement st = (DoWhileStatement) statement;
            return "do " + printStatement(st.body) + " while (" + printExpression(st.test) + ");\n";
        } else if (statement instanceof ExpressionStatement) {
            ExpressionStatement st = (ExpressionStatement) statement;
            return printExpression(st.expr) + ";\n";
        } else if (statement instanceof ForStatement) {
            ForStatement st = (ForStatement) statement;
            return "for(" + printExpression(st.init) + "; " + printExpression(st.test) + "; " +
                    printExpression(st.update) + ") " + printStatement(st.body);
        } else if (statement instanceof FunctionDeclaration) {
            FunctionDeclaration st = (FunctionDeclaration) statement;
            StringBuilder fun = new StringBuilder("function " + st.name + " (");
            boolean c = false;
            for(Identifier id : st.params) {
                fun.append((c ? ", " : "") + id.getName());
                c = true;
            }
            fun.append(") " + printStatement(st.body));
            return fun.toString();
        } else if (statement instanceof IfStatement) {
            IfStatement st = (IfStatement) statement;
            return "if (" + printExpression(st.test) + ")\n" + printStatement(st.consequent) +
                    ((st.alternate != null) ? " else " + printStatement(st.alternate) : "");
        } else if (statement instanceof LabeledStatement) {
            LabeledStatement st = (LabeledStatement) statement;
            return st.label + ":\n" + printStatement(st.body);
        } else if (statement instanceof Return) {
            return "return;\n";
        } else if (statement instanceof Switch) {
            Switch st = (Switch) statement;
            StringBuilder sw = new StringBuilder("switch (" + printExpression(st.expr) + ") {\n");
            boolean c = false;
            for(SwitchCase sc : st.cases) {
                sw.append(printSwitchCase(sc));
            }
            sw.append("}\n");
            return sw.toString();
        } else if (statement instanceof Throw) {
            Throw st = (Throw) statement;
            return "throw " + printExpression(st.expr) + ";\n";
        } else if (statement instanceof VariableDeclaration) {
            VariableDeclaration st = (VariableDeclaration) statement;
            String start = "";
            switch (st.type) {
                case VAR:
                    start = "var ";
                    break;
                case CONST:
                    start = "const ";
                    break;
            }
            return start + st.name + (st.init != null ? printExpression(st.init) : "") + ";";
        } else if (statement instanceof WhileStatement) {
            WhileStatement st = (WhileStatement) statement;
            return "while (" + printExpression(st.test) + ") do " + printStatement(st.body);
        } else {
            return "";
        }
    }

    public static String printSwitchCase(SwitchCase switchCase) {
        StringBuilder sb = new StringBuilder("case " + printExpression(switchCase.test));
        boolean c = false;
        for(Statement s : switchCase.consquent) {
            sb.append(printStatement(s));
        }
        sb.append("\n");
        return sb.toString();
    }
}
