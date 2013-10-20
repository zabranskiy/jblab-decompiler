package com.sdc.cfg.constructions;

import com.sdc.ast.expressions.identifiers.Variable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


public class TryCatch extends Construction {
    private Construction myTryBody;
    private Construction myFinallyBody;
    private Map<Variable, Construction> myCatches = new HashMap<Variable, Construction>();

    @Nullable
    public Construction getTryBody() {
        return myTryBody;
    }

    public void setTryBody(final @NotNull Construction tryBody) {
        this.myTryBody = tryBody;
    }

    @Nullable
    public Construction getFinallyBody() {
        return myFinallyBody;
    }

    public void setFinallyBody(final @NotNull Construction finallyBody) {
        this.myFinallyBody = finallyBody;
    }

    @NotNull
    public Map<Variable, Construction> getCatches() {
        return myCatches;
    }

    public void setCatches(final @NotNull Map<Variable, Construction> catches) {
        this.myCatches = catches;
    }

    public void addCatch(final @NotNull Variable variable, final @NotNull Construction construction) {
        myCatches.put(variable, construction);
    }

    public boolean hasFinallyBlock() {
        return myFinallyBody != null;
    }
}
