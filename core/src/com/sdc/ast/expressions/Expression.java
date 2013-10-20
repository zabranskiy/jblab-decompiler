package com.sdc.ast.expressions;

import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import static com.sdc.ast.ExpressionType.NOT;


public abstract class Expression {
    // true for category 2 (double or long types)
    // false for category 1 (other types)
    protected Type myType;

    protected Expression(final @NotNull Type type) {
        myType = type;
    }

    public final boolean hasDoubleLength() {
        return myType.isDoubleLength();
    }

    @NotNull
    public Expression invert() {
        return new UnaryExpression(NOT, this);
    }

    @NotNull
    public Expression getBase() {
        //for cast and invoke expressions first of all
        return this;
    }

    @NotNull
    public Type getType() {
        return myType;
    }

    public abstract boolean findVariable(final @NotNull Variable variable);

    public final boolean isBoolean() {
        return getType().isBoolean();
    }

    public final void setType(final @NotNull Type type) {
        myType = type;
    }

    public boolean hasNotStaticInvocations() {
        for (final Expression e : getSubExpressions()) {
            if (e.hasNotStaticInvocations()) return true;
        }
        return false;
    }

    @NotNull
    public List<Expression> getSubExpressions() {
        return new ArrayList<Expression>();
    }
}
