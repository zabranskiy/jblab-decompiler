package com.sdc.ast.expressions;

import com.sdc.ast.Type;
import com.sdc.ast.expressions.identifiers.Variable;

import static com.sdc.ast.ExpressionType.NOT;

public abstract class Expression {
    // true for category 2 (double or long types)
    //false for category 1 (other types)
   protected Type myType;

    protected Expression(Type type) {
        myType = type;
    }

    public final boolean hasDoubleLength() {
        return myType.isDoubleLength();
    }

    public Expression invert() {
        return new UnaryExpression(NOT, this);
    }

    public Expression getBase(){
        //for cast and invoke expressions first of all
        return this;
    }

    public Type getType(){
        return myType;
    }
    //public abstract List<Identifier> findIdentifiers();

    public abstract boolean findVariable(Variable variable);
    public final boolean isBoolean(){
        return getType().isBoolean();
    }

    public final void setType(Type type){
        myType = type;
    }
}
