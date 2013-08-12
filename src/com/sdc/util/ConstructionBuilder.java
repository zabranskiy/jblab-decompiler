package com.sdc.util;

import com.sdc.cfg.constructions.Construction;
import com.sdc.cfg.nodes.Node;

import java.util.List;

public class ConstructionBuilder {
    private List<Node> myNodes;
    private final int[] domi;
    private final int[] post;

    public ConstructionBuilder(final List<Node> myNodes, final int[] domi, final int[] post) {
        this.myNodes = myNodes;
        this.domi = domi;
        this.post = post;
    }

    public Construction build() {


     /*   for (Node node : myNodes) {
            if (node instanceof Switch) {
                com.sdc.cfg.constructions.Switch sw = new com.sdc.cfg.constructions.Switch(node.getCondition());

                final int index = myNodes.indexOf(node);
                for (int i = 0; i < myNodes.size(); i++) {
                    if (dominators[i] == index) {
                        boolean isTail = false;
                        for (Node tail : node.getListOfTails()) {
                            if (i == myNodes.indexOf(tail)) {
                                isTail = true;
                                break;
                            }
                        }
                        if (!isTail) {
                            removeLinkFromAllAncestors(myNodes.get(i), true);
                            node.setNextNode(myNodes.get(i));
                            break;
                        }
                    }
                }
                if (node.getNextNode() == null) {
                    Node defaultBranch = ((Switch) node).getNodeByKey(-1);
                    node.removeChild(defaultBranch);
                    defaultBranch.removeAncestor(node);
                    removeLinkFromAllAncestors(defaultBranch, true);
                    defaultBranch.setIsCaseEndNode(false);
                    node.setNextNode(defaultBranch);
                }
                for (final Node tail : node.getListOfTails()) {
                    for (final Node ancestor : tail.getAncestors()) {
                        if (!ancestor.equals(node) && myNodes.indexOf(ancestor) < myNodes.indexOf(tail)) {
                            ancestor.removeChild(tail);
                        }
                    }
                }
            }
            if (node.getCondition() != null) {
                boolean fl1 = true;
                for (Node ancestor : node.getAncestors()) {
                    if (myNodes.indexOf(node) < myNodes.indexOf(ancestor)) {
                        if (dominators[myNodes.indexOf(node)] != dominators[myNodes.indexOf(node.getListOfTails().get(1))]) {
                            node.setNextNode(node.getListOfTails().get(1));
                        }
                        removeLinkFromAllAncestors(node.getListOfTails().get(1), false);
                        removeLinkFromAllAncestors(node, false);
                        fl1 = false;
                        myNodes.set(myNodes.indexOf(node), new While(node));
                        break;
                    }
                }

                if (fl1 && myNodes.indexOf(node) < myNodes.indexOf(node.getListOfTails().get(1))) {
                    boolean fl = false;
                    for (Node ancestor : node.getAncestors()) {
                        if (myNodes.indexOf(ancestor) > myNodes.indexOf(node)) {
                            fl = true;
                            break;
                        }
                    }

                    if (!fl) {
                        final int index = myNodes.indexOf(node);
                        for (int i = 0; i < myNodes.size(); i++) {
                            if (dominators[i] == index) {
                                boolean isTail = false;
                                for (Node tail : node.getListOfTails()) {
                                    if (i == myNodes.indexOf(tail)) {
                                        isTail = true;
                                        break;
                                    }
                                }
                                if (!isTail) {
                                    removeLinkFromAllAncestors(myNodes.get(i), false);
                                    node.setNextNode(myNodes.get(i));
                                    break;
                                }
                            }
                        }
                        if (node.getNextNode() == null) {
                            node.setNextNode(node.getListOfTails().get(1));
                            removeLinkFromAllAncestors(node.getListOfTails().get(1), false);
                            node.addTail(node.getNextNode());
                        }
                    }
                }
            }
        }*/

        return null;
    }
}
