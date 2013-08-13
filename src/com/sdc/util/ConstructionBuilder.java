package com.sdc.util;

import com.sdc.cfg.constructions.Construction;
import com.sdc.cfg.constructions.ElementaryBlock;
import com.sdc.cfg.nodes.Node;
import com.sdc.cfg.nodes.Switch;

import java.util.List;

public class ConstructionBuilder {
    private List<Node> myNodes;
    private final int[] domi;
    private final int[] post;
    private int[] intersection;

    public ConstructionBuilder(final List<Node> myNodes, final int[] domi, final int[] post) {
        this.myNodes = myNodes;
        this.domi = domi;
        this.post = post;
        if (domi != null && domi.length > 1) {
            this.intersection = new int[domi.length];
            for (int i = 0; i < domi.length; i++) {
                intersection[i] = domi[i] == post[i] ? domi[i] : -1;
            }
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

            Node nextNode = findNextNodeToSwitchWithDefaultCase(switchNode);

            if (nextNode == null) {
                List<Node> tails = switchNode.getListOfTails();
                Node past = switchNode.getListOfTails().get(0);
                for (int i = 1; i < tails.size(); i++) {
                    switchConstruction.addCase(switchNode.getKeys()[i - 1], new ConstructionBuilder(myNodes.subList(getRelativeIndex(past), getRelativeIndex(tails.get(i))), domi, post).build());
                    past = tails.get(i);
                }

                nextNode = findNextNodeToSwitchWithoutDefaultCase(switchNode);
            } else {
                switchNode.addTail(nextNode);

                List<Node> tails = switchNode.getListOfTails();
                Node past = switchNode.getListOfTails().get(0);
                Node defaultNode = switchNode.getNodeByKey(-1);
                for (int i = 1; i < tails.size(); i++) {
                    if (tails.get(i).getIndex() == defaultNode.getIndex()) {
                        switchConstruction.addCase(switchNode.getKeys()[i - 1], new ConstructionBuilder(myNodes.subList(getRelativeIndex(past), getRelativeIndex(defaultNode)), domi, post).build());
                        i++;
                        switchConstruction.setDefaultCase(new ConstructionBuilder(myNodes.subList(getRelativeIndex(defaultNode), getRelativeIndex(tails.get(i))), domi, post).build());
                    } else {
                        Construction construction = new ConstructionBuilder(myNodes.subList(getRelativeIndex(past), getRelativeIndex(tails.get(i))), domi, post).build();
                        if (past.getIndex() == defaultNode.getIndex()) {
                            switchConstruction.setDefaultCase(construction);
                        } else {
                            switchConstruction.addCase(switchNode.getKeys()[i - 1], construction);
                        }
                    }
                    past = tails.get(i);
                }

                switchNode.removeChild(nextNode);
            }

            addBreakToAncestors(nextNode);

            if (getRelativeIndex(nextNode.getIndex()) < myNodes.size()) {
                switchNode.setNextNode(nextNode);
                extractNextConstruction(switchConstruction, switchNode);
            }

            return switchConstruction;
        }
        return null;
    }

    private Node findNextNodeToSwitchWithDefaultCase(Switch switchNode) {
        Node result = null;

        final int initialDominator = domi[switchNode.getIndex()];
        final List<Node> tails = switchNode.getListOfTails();
        final int startIndex = tails.get(tails.size() - 1).getIndex() + 1;

        for (int i = startIndex; i < domi.length; i++) {
            if (domi[i] == switchNode.getIndex()) {
                result = myNodes.get(getRelativeIndex(i));
                break;
            }
        }

//        if (result == null) {
//            for (int i = startIndex; i < domi.length; i++) {
//                if (domi[i] == initialDominator) {
//                    if (myNodes.size() > getRelativeIndex(i)) {
//                        result = myNodes.get(getRelativeIndex(i));
//                    }
//                    break;
//                }
//            }
//        }

        return result;
    }

    private Node findNextNodeToSwitchWithoutDefaultCase(Switch switchNode) {
        Node defaultBranch = switchNode.getNodeByKey(-1);

        switchNode.removeChild(defaultBranch);
        defaultBranch.removeAncestor(switchNode);

        return defaultBranch;
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

    private int getRelativeIndex(Node node) {
        return getRelativeIndex(node.getIndex());
    }

    private int getRelativeIndex(int index) {
        return index - myNodes.get(0).getIndex();
    }

    private void extractNextConstruction(Construction construction, final Node currentNode) {
        construction.setNextConstruction(build(currentNode.getNextNode()));
    }

    protected void addBreakToAncestors(final Node child) {
        for (final Node parent : child.getAncestors()) {
            if (parent.getConstruction() != null) {
                parent.getConstruction().setBreak("");
            }
        }
    }
}
