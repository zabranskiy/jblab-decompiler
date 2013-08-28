package com.sdc.kotlin;

import com.sdc.ast.Type;
import com.sdc.ast.expressions.Constant;
import com.sdc.ast.expressions.identifiers.Variable;
import com.sdc.util.DeclarationWorker;

public class KotlinVariable extends Variable {
    public static final String SHARED_VAR_IDENTIFIER = "SharedVar.";

    private boolean myIsNotNull = false;
    private boolean myIsInForDeclaration = false;

    public void setIsInForDeclaration(final boolean isInForDeclaration) {
        this.myIsInForDeclaration = isInForDeclaration;
    }

    public static boolean isSharedVar(final String type) {
        return type.startsWith(SHARED_VAR_IDENTIFIER);
    }

    public KotlinVariable(final int index, final Type variableType, final String name) {
        super(index, variableType, name);
    }

    public boolean isNotNull() {
        return myIsNotNull;
    }

    public void setIsNotNull(final boolean isNotNull) {
        this.myIsNotNull = isNotNull;
    }

    @Override
    public Constant getName() {
        Object value = super.getName().getValue();
        final String name = value==null?null:value.toString();
        final String actualName = myIsMethodParameter || myIsInForDeclaration ? name : "var " + name;

        return myIsDeclared ? myName : new Constant(actualName, false, myType);
    }

    @Override
    public Type getType() {
        String myVariableType = myType.toString(KotlinOperationPrinter.getInstance());
        boolean notNeedNullableMark = myIsNotNull ||  DeclarationWorker.isPrimitiveClass(myVariableType) || myVariableType.endsWith("?");

        if (isSharedVar(myVariableType)) {
            myVariableType = DeclarationWorker.convertJavaPrimitiveClassToKotlin(myVariableType.substring(SHARED_VAR_IDENTIFIER.length()));
            if (!myVariableType.equals("Any")) {
                notNeedNullableMark = true;
            }
        }

        final String nullableMark = notNeedNullableMark ? "" : "?";

        return new Type(myVariableType + nullableMark);
    }

    public Type getActualType() {
        return super.getType();
    }

    @Override
    protected Variable createVariable(final int index, final Type variableType, final String name) {
        return new KotlinVariable(index, variableType, name);
    }
}
