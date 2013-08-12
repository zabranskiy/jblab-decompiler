package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.identifiers.Variable;

import java.util.HashMap;
import java.util.Map;

public class TryCatch extends Construction {
    private Construction myTryBody;
    private Construction myFinallyBody;
    private Map<Variable, Construction> myCatches = new HashMap<Variable, Construction>();

    public Construction getTryBody() {
        return myTryBody;
    }

    public void setTryBody(final Construction tryBody) {
        this.myTryBody = tryBody;
    }

    public Construction getFinallyBody() {
        return myFinallyBody;
    }

    public void setFinallyBody(final Construction finallyBody) {
        this.myFinallyBody = finallyBody;
    }

    public Map<Variable, Construction> getCatches() {
        return myCatches;
    }

    public void setCatches(final Map<Variable, Construction> catches) {
        this.myCatches = catches;
    }

    public void addCatch(final Variable variable, final Construction construction) {
        myCatches.put(variable, construction);
    }

    public boolean hasFinallyBlock() {
        return myFinallyBody != null;
    }
}
