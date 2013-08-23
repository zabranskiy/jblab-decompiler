package com.sdc.kotlin;

import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.NewArray;

import java.util.List;

public class KotlinNewArray extends NewArray {
    private Expression myInitializer;

    public KotlinNewArray(final int dimensionsCount, final String type, final List<Expression> dimensions) {
        super(dimensionsCount, type, dimensions);
    }

    public Expression getInitializer() {
        return myInitializer;
    }

    public void setInitializer(final Expression initializer) {
        this.myInitializer = initializer;
    }
}
