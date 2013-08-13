package com.sdc.util;

import com.sdc.cfg.constructions.Construction;
import com.sdc.cfg.constructions.ElementaryBlock;
import com.sdc.cfg.nodes.Node;
import com.sdc.cfg.nodes.Switch;

import java.util.List;

public class ConstructionBuilder {
    private List<Node> myNodes;
    private int size;
    private final int[] domi;
    private final int[] post;
    private int[] intersection;

    public ConstructionBuilder(final List<Node> myNodes, final int[] domi, final int[] post) {
        this.myNodes = myNodes;
        this.size = myNodes.size();
        this.domi = domi;
        this.post = post;
        this.intersection = new int[size];
        if (size > 1)
            for (int i = 0; i < size; i++) {
                intersection[i] = domi[i] == post[i] ? domi[i] : -1;
            }
    }

    public Construction build() {
        return build(myNodes.get(0));
    }

    private Construction build(Node node) {
        Construction elementaryBlock = extractElementaryBlock(node);
        Construction currentConstruction = extractConstruction(node);

        if (node.getCondition() == null) {
            node.setConstruction(elementaryBlock);
        } else {
            node.setConstruction(currentConstruction);
        }

        elementaryBlock.setNextConstruction(currentConstruction);
        return elementaryBlock;
    }

    private Construction extractConstruction(Node node) {
        Construction result;

        result = extractConditionBlock(node);
        if (result != null) {
            return result;
        }

        result = extractSwitch(node);
        if (result != null) {
            return result;
        }

        return null;
    }

    private Construction extractElementaryBlock(final Node node) {
        ElementaryBlock elementaryBlock = new ElementaryBlock();
        elementaryBlock.setStatements(node.getStatements());
        return elementaryBlock;
    }

    private Construction extractSwitch(Node node) {
        if (node instanceof Switch) {
            Switch switchNode = (Switch) node;
            com.sdc.cfg.constructions.Switch switchConstruction = new com.sdc.cfg.constructions.Switch(switchNode.getExpr());

            for (int i = 0; i < size; i++) {
                if (domi[i] == node.getIndex()) {
                    boolean isTail = false;
                    for (Node tail : node.getListOfTails()) {
                        if (i == tail.getIndex()) {
                            isTail = true;
                            break;
                        }
                    }
                    if (!isTail) {
//                        new ConstructionBuilder().build();
//                        removeLinkFromAllAncestors(myNodes.get(i), true);
                        node.setNextNode(myNodes.get(i));
                        break;
                    }
                }
            }
            if (node.getNextNode() == null) {
                List<Node> tails = node.getListOfTails();
                Node past = node.getListOfTails().get(0);
                for (int i = 1; i < tails.size(); i++) {
                    switchConstruction.addCase(switchNode.getKeys()[i - 1], new ConstructionBuilder(myNodes.subList(past.getIndex(), tails.get(i).getIndex()), domi, post).build());
                    past = tails.get(i);
                }
            } else {
                node.addTail(node.getNextNode());

                List<Node> tails = node.getListOfTails();
                Node past = node.getListOfTails().get(0);
                int defaultIndex = switchNode.getNodeByKey(-1).getIndex();
                for (int i = 1; i < tails.size(); i++) {
                    if (tails.get(i).getIndex() == defaultIndex) {
                        switchConstruction.addCase(switchNode.getKeys()[i - 1], new ConstructionBuilder(myNodes.subList(past.getIndex(), defaultIndex), domi, post).build());
                        i++;
                        switchConstruction.setDefaultCase(new ConstructionBuilder(myNodes.subList(defaultIndex, tails.get(i).getIndex()), domi, post).build());
                    } else {
                        Construction construction = new ConstructionBuilder(myNodes.subList(past.getIndex(), tails.get(i).getIndex()), domi, post).build();
                        if (past.getIndex() == defaultIndex) {
                            switchConstruction.setDefaultCase(construction);
                        } else {
                            switchConstruction.addCase(switchNode.getKeys()[i - 1], construction);
                        }
                    }
                    past = tails.get(i);
                }
            }

/*
            if (node.getNextNode() == null) {
                Node defaultBranch = ((Switch) node).getNodeByKey(-1);
                node.removeChild(defaultBranch);
                defaultBranch.removeAncestor(node);
                removeLinkFromAllAncestors(defaultBranch, true);
                defaultBranch.setIsCaseEndNode(false);
                node.setNextNode(defaultBranch);
            }
*/
//            for (final Node tail : node.getListOfTails()) {
//                for (final Node ancestor : tail.getAncestors()) {
//                    if (!ancestor.equals(node) && myNodes.indexOf(ancestor) < myNodes.indexOf(tail)) {
//                        ancestor.removeChild(tail);
//                    }
//                }
//            }

            extractNextConstruction(switchConstruction, node);
            return switchConstruction;
        }
        return null;
    }

    private Construction extractConditionBlock(Node node) {
               /* if (node.getCondition() != null) {
            boolean fl1 = true;
            for (Node ancestor : node.getAncestors()) {
                if (myNodes.indexOf(node) < myNodes.indexOf(ancestor)) {
                    if (domi[myNodes.indexOf(node)] != domi[myNodes.indexOf(node.getListOfTails().get(1))]) {
                        node.setNextNode(node.getListOfTails().get(1));
                    }
//                    removeLinkFromAllAncestors(node.getListOfTails().get(1), false);
//                    removeLinkFromAllAncestors(node, false);
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
                    for (int i = 0; i < size; i++) {
                        if (domi[i] == index) {
                            boolean isTail = false;
                            for (Node tail : node.getListOfTails()) {
                                if (i == myNodes.indexOf(tail)) {
                                    isTail = true;
                                    break;
                                }
                            }
                            if (!isTail) {
//                                removeLinkFromAllAncestors(myNodes.get(i), false);
                                node.setNextNode(myNodes.get(i));
                                break;
                            }
                        }
                    }
                    if (node.getNextNode() == null) {
                        node.setNextNode(node.getListOfTails().get(1));
//                        removeLinkFromAllAncestors(node.getListOfTails().get(1), false);
                        node.addTail(node.getNextNode());
                    }
                }
            }
        }*/
        return null;
    }

    private void extractNextConstruction(Construction construction, final Node currentNode) {
        construction.setNextConstruction(build(currentNode.getNextNode()));
    }

    protected void removeLinkFromAllAncestors(final Node child) {
        for (final Node parent : child.getAncestors()) {
            if (parent.getCondition() == null) {
                parent.removeChild(child);
//                child.removeAncestor(parent);
//                parent.setIsCaseEndNode(needToBreak);
            }
        }
    }
}
