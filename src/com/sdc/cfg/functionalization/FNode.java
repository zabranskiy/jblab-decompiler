package com.sdc.cfg.functionalization;

import com.sdc.ast.controlflow.Return;
import com.sdc.ast.controlflow.Statement;
import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.Invocation;
import com.sdc.cfg.Node;

import java.util.ArrayList;
import java.util.List;

public class FNode extends Node {
    private final List<Statement> myBranches = new ArrayList<Statement>();
    private final FType ftype;
    private final int index;

    public enum FType {
        SWITCH, IF, SIMPLE
    }

    public FNode(final FType ftype, final int index) {
        this.index = index;
        this.ftype = ftype;
    }

    public void addBranch(final int index) {
        myBranches.add(new Return(new Invocation((index == 0) ? "start" : "fnode_" + index, "", null)));
    }

    public FType getType() {
        return ftype;
    }

    public int getIndex() {
        return index;
    }

    public List<Statement> getBranches() {
        return myBranches;
    }
}