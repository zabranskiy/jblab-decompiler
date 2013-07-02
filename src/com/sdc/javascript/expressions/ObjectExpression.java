package com.sdc.javascript.expressions;

import java.util.List;

public class ObjectExpression extends Expression {
    public final List<Property> properties;

    public ObjectExpression(List<Property> properties) {
        this.properties = properties;
    }
}
