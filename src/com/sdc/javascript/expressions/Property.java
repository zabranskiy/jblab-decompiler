package com.sdc.javascript.expressions;

public class Property {
    public final Identifier key;
    public final Expression value;

    public Property(Identifier key, Expression value) {
        this.key = key;
        this.value = value;
    }
}
