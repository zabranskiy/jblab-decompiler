package com.sdc.ast.expressions;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class Constant extends PriorityExpression {
    public static Constant NULL = new Constant("null", false, new Type("Object"));

    private final Object myValue;
    private final boolean myIsStringValue;

    public Constant(final @NotNull Object value, final boolean isStringValue, final @NotNull Type type) {
        super(ExpressionType.CONST, type);
        this.myValue = value;
        this.myIsStringValue = isStringValue;
    }

    @NotNull
    public Object getValue() {
        return myValue;
    }

    public boolean isStringValue() {
        return myIsStringValue;
    }

    @NotNull
    @Override
    public String toString() {
        return "Constant{" + "myValue=" + myValue + '}';
    }

    @NotNull
    public String valueToString() {
        return myValue.toString();
    }

    public boolean isThis() {
        return myValue.equals("this") && !myIsStringValue;
    }

    public boolean isNull() {
        return myValue.toString().contains("null") && !myIsStringValue;
    }

    @Override
    public boolean findVariable(final @NotNull Variable variable) {
        return false;
    }

    @NotNull
    @Override
    public List<Expression> getSubExpressions() {
        return new ArrayList<Expression>();
    }
}
