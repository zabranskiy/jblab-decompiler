package com.sdc.ast.expressions;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;

public class Constant extends PriorityExpression {
    private final Object myValue;
    private final boolean myIsStringValue;
    public static Constant NULL = new Constant("null", false, new Type("Object"));


/*    public Constant(final Object value, final boolean isStringValue) {
        this(value, isStringValue, false);
    }*/

    public Constant(final Object value, final boolean isStringValue, Type type) {
        super(ExpressionType.CONST,type);
        this.myValue = value;
        this.myIsStringValue = isStringValue;
    }

    public Object getValue() {
        return myValue;
    }

    public boolean isStringValue() {
        return myIsStringValue;
    }

    @Override
    public String toString() {
        return "Constant{" +
                "myValue=" + myValue +
                '}';
    }

    public String valueToString(){
        return myValue.toString();
    }

    public boolean isThis(){
        return myValue.equals("this") && !myIsStringValue;
    }

    public boolean isNull(){
        return ((myValue==null) || myValue.toString().contains("null")) && !myIsStringValue;
    }

}
