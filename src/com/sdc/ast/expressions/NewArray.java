package com.sdc.ast.expressions;

import java.util.List;

public class NewArray extends Expression {
    private final int myDimensionsCount;
    private final String myType;
    private final List<Expression> myDimensions;

    public NewArray(int dimensionsCount, String type, List<Expression> dimensions) {
        this.myDimensionsCount = dimensionsCount;
        this.myType = type;
        this.myDimensions = dimensions;
    }

    public List<Expression> getDimensions() {
        return myDimensions;
    }

    public String getType() {
        return myType;
    }
}
