package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.identifiers.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TryCatch extends Construction {
    private List<Construction> myTryBody = new ArrayList<Construction>();
    private List<Construction> myFinallyBody = new ArrayList<Construction>();
    private Map<Variable, List<Construction>> myCatches = new HashMap<Variable, List<Construction>>();

    public List<Construction> getTryBody() {
        return myTryBody;
    }

    public void setTryBody(final List<Construction> tryBody) {
        this.myTryBody = tryBody;
    }

    public List<Construction> getFinallyBody() {
        return myFinallyBody;
    }

    public void setFinallyBody(final List<Construction> finallyBody) {
        this.myFinallyBody = finallyBody;
    }

    public Map<Variable, List<Construction>> getCatches() {
        return myCatches;
    }

    public void setCatches(final Map<Variable, List<Construction>> catches) {
        this.myCatches = catches;
    }

    public void addCatch(final Variable variable, final List<Construction> constructions) {
        myCatches.put(variable, constructions);
    }

    public boolean hasFinallyBlock() {
        return !myFinallyBody.isEmpty();
    }
}
