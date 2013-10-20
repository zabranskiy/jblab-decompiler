package com.sdc.cfg.functionalization;

import com.sdc.ast.controlflow.Return;
import com.sdc.ast.controlflow.Statement;
import com.sdc.ast.expressions.Invocation;

import com.sdc.cfg.nodes.Node;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class FNode extends Node {
    private final List<Statement> myBranches = new ArrayList<Statement>();
    private final FType myFType;
    private final int myIndex;

    public enum FType {
        SWITCH, IF, SIMPLE
    }

    public FNode(final @NotNull FType ftype, final int index) {
        this.myIndex = index;
        this.myFType = ftype;
    }

    public void addBranch(final int index) {
        //TODO: remove maybe first null
        myBranches.add(new Return(new Invocation((index == 0) ? "start" : "fnode_" + index, null, null)));
    }

    @NotNull
    public FType getType() {
        return myFType;
    }

    public int getIndex() {
        return myIndex;
    }

    @NotNull
    public List<Statement> getBranches() {
        return myBranches;
    }
}