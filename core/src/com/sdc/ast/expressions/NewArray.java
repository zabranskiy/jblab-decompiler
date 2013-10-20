package com.sdc.ast.expressions;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class NewArray extends PriorityExpression {
    private final int myDimensionsCount;
    private final List<Expression> myDimensions;
    private final List<Expression> myInitializationValues;

    public NewArray(final int dimensionsCount, final @NotNull String type, final @NotNull List<Expression> dimensions) {
        super(ExpressionType.NEWARRAY, new Type(type, dimensionsCount));
        this.myDimensionsCount = dimensionsCount;
        this.myDimensions = dimensions;
        this.myInitializationValues = new ArrayList<Expression>();
    }

    @NotNull
    public List<Expression> getDimensions() {
        return myDimensions;
    }

    public void addNewInitializationValue(final @NotNull Expression e) {
        myInitializationValues.add(e);
    }

    public boolean hasInitialization() {
        return !myInitializationValues.isEmpty();
    }

    @NotNull
    public List<Expression> getInitializationValues() {
        return myInitializationValues;
    }

    public int getDimensionsCount() {
        return myDimensionsCount;
    }

    @Override
    public boolean findVariable(final @NotNull Variable variable) {
        boolean res = false;
        for (final Expression expression : myDimensions) {
            res = res || expression.findVariable(variable);
        }
        for (Expression expression : myInitializationValues) {
            res = res || expression.findVariable(variable);
        }
        return res;
    }

    @NotNull
    @Override
    public List<Expression> getSubExpressions() {
        final List<Expression> subExpressions = new ArrayList<Expression>();
        subExpressions.addAll(myDimensions);
        return subExpressions;
    }
}
