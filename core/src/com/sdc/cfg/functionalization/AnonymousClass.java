package com.sdc.cfg.functionalization;

import java.util.ArrayList;
import java.util.List;

public class AnonymousClass {

    private List<FNode> myFNodes = new ArrayList<FNode>();

    public AnonymousClass() {
    }

    public void addFNode(final FNode node) {
        myFNodes.add(node);
    }

    public List<FNode> getFNodes() {
        return myFNodes;
    }

}
