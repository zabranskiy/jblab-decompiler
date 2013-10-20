package com.sdc.ast.expressions;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class Invocation extends PriorityExpression {
    private final String myFunction;
    private final List<Expression> myArguments;

    public Invocation(final @NotNull String function, final @NotNull Type type, final @NotNull List<Expression> arguments) {
        super(ExpressionType.INVOCATION, type);
        this.myFunction = function;
        this.myArguments = arguments;
    }

    @NotNull
    public String getFunction() {
        return myFunction;
    }

    @NotNull
    public List<Expression> getArguments() {
        return myArguments;
    }

    @Override
    public boolean findVariable(final @NotNull Variable variable) {
        boolean res = false;
        for (final Expression expression : myArguments) {
            res = res || expression.findVariable(variable);
        }
        return res;
    }

    @NotNull
    @Override
    public List<Expression> getSubExpressions() {
        final List<Expression> subExpressions = new ArrayList<Expression>();
        subExpressions.addAll(myArguments);
        return subExpressions;
    }
}
