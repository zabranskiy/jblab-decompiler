package com.sdc.cfg.constructions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class SwitchCase extends Construction {
    private List<Integer> myKeys = new ArrayList<Integer>();
    private final Construction myCaseBody;

    public SwitchCase(final @NotNull Construction caseBody) {
        this.myCaseBody = caseBody;
    }

    public void addKey(final @NotNull Integer key) {
        myKeys.add(key);
    }

    public void setKeys(final @NotNull List<Integer> keys) {
        this.myKeys = keys;
    }

    @NotNull
    public List<Integer> getKeys() {
        return myKeys;
    }

    @NotNull
    public Construction getCaseBody() {
        return myCaseBody;
    }
}
