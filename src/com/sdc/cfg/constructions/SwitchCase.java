package com.sdc.cfg.constructions;

import java.util.ArrayList;
import java.util.List;

public class SwitchCase extends Construction {
    private List<Integer> myKeys = new ArrayList<Integer>();
    private final Construction myCaseBody;

    public SwitchCase(final Construction caseBody) {
        this.myCaseBody = caseBody;
    }

    public void addKey(final Integer key) {
        myKeys.add(key);
    }

    public void setKeys(final List<Integer> keys) {
        this.myKeys = keys;
    }

    public List<Integer> getKeys() {
        return myKeys;
    }

    public Construction getCaseBody() {
        return myCaseBody;
    }
}
