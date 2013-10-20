package com.sdc.cfg.functionalization;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class AnonymousClass {
    private final List<FNode> myFNodes = new ArrayList<FNode>();

    public AnonymousClass() {
    }

    public void addFNode(final @NotNull FNode node) {
        myFNodes.add(node);
    }

    @NotNull
    public List<FNode> getFNodes() {
        return myFNodes;
    }
}
