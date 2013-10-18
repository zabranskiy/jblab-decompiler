package com.sdc.ast.expressions;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;

import java.util.ArrayList;
import java.util.List;

public class Invocation extends PriorityExpression {
    private final String myFunction;
    private final List<Expression> myArguments;

    public Invocation(final String function, final Type type, final List<Expression> arguments) {
        super(ExpressionType.INVOCATION, type);
        this.myFunction = function;
        this.myArguments = arguments;
    }

    public String getFunction() {
        return myFunction;
    }

    public List<Expression> getArguments() {
        return myArguments;
    }
    @Override
    public boolean findVariable(Variable variable) {
        boolean res=false;
        for(Expression expression: myArguments){
            res=res || expression.findVariable(variable);
        }
        return res;
    }

    @Override
    public List<Expression> getSubExpressions() {
        List<Expression> subExpressions = new ArrayList<Expression>();
        subExpressions.addAll(myArguments);
        return subExpressions;
    }
}
