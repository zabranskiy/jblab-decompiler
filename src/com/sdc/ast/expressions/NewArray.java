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
        setDoubleLength(false);
    }

    public List<Expression> getDimensions() {
        return myDimensions;
    }

    public String getType() {
        return myType;
    }

    public String getFullType() {
        StringBuilder result= new StringBuilder(myType);
        for (int i = 0; i < myDimensionsCount; i++) {
            result.append("[]");
        }
        return result.append(" ").toString();
    }
}
