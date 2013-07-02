package com.sdc.cfg;

import com.sdc.ast.expressions.Expression;
import org.objectweb.asm.Label;

import java.util.ArrayList;
import java.util.List;

public class Switch extends Node {
    private final Expression myExpr;
    private final int[] myKeys;
    private List<Label> myLabels = new ArrayList<Label>();

    public Switch(Expression myExpr, int[] myKeys, List<Label> labels) {
        this.myExpr = myExpr;
        this.myKeys = myKeys;
        this.myLabels = labels;
    }

    public List<Label> getLabels() {
        return myLabels;
    }

    public Node getDefault() {
        for (Node node : this.getListOfTails()) {
            if (node.containsLabel(myLabels.get(myLabels.size() - 1))) {
                return node;
            }
        }
        return null;
    }

    public int[] getKeys() {
        return myKeys;
    }

    public Expression getExpr() {
        return myExpr;
    }

}
