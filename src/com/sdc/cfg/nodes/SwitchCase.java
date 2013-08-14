package com.sdc.cfg.nodes;

import java.util.ArrayList;
import java.util.List;

public class SwitchCase implements Comparable<SwitchCase> {
    private List<Integer> myKeys = new ArrayList<Integer>();
    private final Node myCaseBody;

    public SwitchCase(final Node caseBody) {
        this.myCaseBody = caseBody;
    }

    public void addKey(final Integer key) {
        myKeys.add(key);
    }

    public List<Integer> getKeys() {
        return myKeys;
    }

    public Node getCaseBody() {
        return myCaseBody;
    }

    public int compareTo(SwitchCase switchCase) {
        final int thisIndex = this.getCaseBody().getIndex();
        final int anotherIndex = switchCase.getCaseBody().getIndex();

        return thisIndex == anotherIndex ? 0 : thisIndex > anotherIndex ? 1 : -1;
    }
}
