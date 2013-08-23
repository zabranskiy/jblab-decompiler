package com.sdc.ast.expressions;

import com.sdc.ast.OperationType;

import java.util.ArrayList;
import java.util.List;

public class NewArray extends PriorityExpression {
    private final int myDimensionsCount;
    private final String myArrayType;
    private final List<Expression> myDimensions;
    private final List<Expression> myInitializationValues;

    public NewArray(int dimensionsCount, String type, List<Expression> dimensions) {
        this.myDimensionsCount = dimensionsCount;
        this.myArrayType = type;
        this.myDimensions = dimensions;
        myInitializationValues = new ArrayList<Expression>();
        setDoubleLength(false);
        myType = OperationType.NEWARRAY;

    }

    public List<Expression> getDimensions() {
        return myDimensions;
    }

    public String getType() {
        return myArrayType;
    }

    public String getFullType() {
        StringBuilder result = new StringBuilder(myArrayType);
        for (int i = 0; i < myDimensionsCount; i++) {
            result.append("[]");
        }
        return result.append(" ").toString();
    }

    @Override
    public boolean isBoolean() {
        return false;
    }

    public void addNewInitializationValue(Expression e){
        myInitializationValues.add(e);
    }

    public boolean hasInitialization(){
        return !myInitializationValues.isEmpty();
    }

    public List<Expression> getInitializationValues(){
        return myInitializationValues;
    }
}
