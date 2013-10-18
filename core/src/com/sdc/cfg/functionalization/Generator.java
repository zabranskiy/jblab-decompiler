package com.sdc.cfg.functionalization;

import com.sdc.ast.controlflow.Statement;
import com.sdc.cfg.nodes.Node;
import com.sdc.cfg.nodes.Switch;

import java.util.ArrayList;
import java.util.List;

public class Generator {
    private final List<Node> myNodes;
    private final AnonymousClass aClass = new AnonymousClass();

    public Generator(final List<Node> nodes) {
        this.myNodes = nodes;
    }

    public AnonymousClass genAnonymousClass() {
        for (Node node : myNodes) {
            final FNode.FType ftype = (node instanceof Switch) ? FNode.FType.SWITCH
                    : (node.getCondition() != null) ? FNode.FType.IF :
                    FNode.FType.SIMPLE;
            final FNode fnode = new FNode(ftype, myNodes.indexOf(node));
            fnode.setStatements(new ArrayList<Statement>(node.getStatements()));
            for (Node tail : node.getListOfTails()) {
                fnode.addBranch(myNodes.indexOf(tail));
            }
            aClass.addFNode(fnode);
        }
        return aClass;
    }
}
