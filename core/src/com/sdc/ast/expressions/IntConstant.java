package com.sdc.ast.expressions;

import com.sdc.ast.Type;


public class IntConstant extends Constant {
    public static final Constant ZERO = new IntConstant(0);
    public static final Constant ONE = new IntConstant(1);
    public static final Constant M_ONE = new IntConstant(-1);

    private int myIntValue = 0;

    public IntConstant(final int value) {
        super(value, false, Type.INT_TYPE);
        this.myIntValue = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final IntConstant that = (IntConstant) o;

        if (myIntValue != that.myIntValue) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return myIntValue;
    }

    public boolean isOne() {
        return equals(ONE);
    }

    public boolean isMinusOne() {
        return equals(M_ONE);
    }

    public boolean isZero() {
        return equals(ZERO);
    }

    public boolean isPosInt() {
        return myIntValue > 0;
    }

    public boolean isNegInt() {
        return myIntValue < 0;
    }

    public int getIntValue() {
        return myIntValue;
    }
}
