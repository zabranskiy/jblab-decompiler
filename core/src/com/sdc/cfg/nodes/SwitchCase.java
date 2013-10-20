package com.sdc.cfg.nodes;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class SwitchCase implements Comparable<SwitchCase> {
    private final List<Integer> myKeys = new ArrayList<Integer>();
    private final Node myCaseBody;

    public SwitchCase(final @NotNull Node caseBody) {
        this.myCaseBody = caseBody;
    }

    public void addKey(final @Nullable Integer key) {
        myKeys.add(key);
    }

    @NotNull
    public List<Integer> getKeys() {
        return myKeys;
    }

    @NotNull
    public Node getCaseBody() {
        return myCaseBody;
    }

    public int compareTo(final @NotNull SwitchCase switchCase) {
        final int thisIndex = this.getCaseBody().getIndex();
        final int anotherIndex = switchCase.getCaseBody().getIndex();

        return thisIndex == anotherIndex ? 0 : thisIndex > anotherIndex ? 1 : -1;
    }
}
