package com.sdc.ast.expressions;

import com.sdc.ast.ExpressionType;
import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;

import java.util.ArrayList;
import java.util.List;

public class NewArray extends PriorityExpression {
    private final int myDimensionsCount;
    private final List<Expression> myDimensions;
    private final List<Expression> myInitializationValues;

    public NewArray(int dimensionsCount, String type, List<Expression> dimensions) {
        super(ExpressionType.NEWARRAY, new Type(type,dimensionsCount));
        this.myDimensionsCount = dimensionsCount;
        this.myDimensions = dimensions;
        myInitializationValues = new ArrayList<Expression>();
    }

    public List<Expression> getDimensions() {
        return myDimensions;
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

    public int getDimensionsCount() {
        return myDimensionsCount;
    }
    @Override
    public boolean findVariable(Variable variable) {
        boolean res=false;
        for(Expression expression: myDimensions){
            res = res || expression.findVariable(variable);
        }
        for(Expression expression: myInitializationValues){
            res = res || expression.findVariable(variable);
        }
        return res;
    }
}
