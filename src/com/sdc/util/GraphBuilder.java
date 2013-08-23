package com.sdc.util;

import com.sdc.ast.expressions.BinaryExpression;
import com.sdc.cfg.nodes.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphBuilder {
    private List<Node> myNodes;
    private final int size;

    public GraphBuilder(final List<Node> myNodes) {
        this.myNodes = myNodes;
        this.size = myNodes.size();
    }


    public void rebuild() {
        int i = 0;
        while (i < size) {
            Node node = myNodes.get(i++);
            if (node.getCondition() != null) {
                i = parseConditions(node);
            }
        }
    }

/*    private class ConditionalTreeNode {
        private int index;
        private int leftNodeIndex;
        private int rightNodeIndex;

        private ConditionalTreeNode(int index, int leftNodeIndex) {
            this.index = index;
            this.leftNodeIndex = leftNodeIndex;
        }

        private ConditionalTreeNode(int index, int leftNodeIndex, int rightNodeIndex) {
            this.index = index;
            this.leftNodeIndex = leftNodeIndex;
            this.rightNodeIndex = rightNodeIndex;
        }
    }*/

    private int parseConditions(Node start) {
        HashMap<Integer, ArrayList<Integer>> tree = new HashMap<Integer, ArrayList<Integer>>();
        ArrayList<Integer> list;
        List<Node> nodes = new ArrayList<Node>();

        nodes.add(start);
        Node current = start;
        Node inner = start.getListOfTails().get(0);
        while ((inner.getCondition() != null && inner.getStatements().isEmpty()) || (inner.getCondition() == null && inner.getInnerLabels().isEmpty() && inner.getStatements().isEmpty())) {
            if ((inner.getCondition() != null && inner.getStatements().isEmpty())) {
                nodes.add(inner);
                current = inner;
            }
            inner = inner.getListOfTails().get(0);
        }
        Node exit = current.getListOfTails().get(1);

        for (Node node : nodes) {
            list = new ArrayList<Integer>();
            Node left = node.getListOfTails().get(0);
            if (left.getCondition() == null) {
                ArrayList<Integer> list2 = new ArrayList<Integer>();
                list2.add(left.getListOfTails().get(0).getIndex());
                tree.put(left.getIndex(), list2);
            }
            list.add(left.getIndex());
            Node right = node.getListOfTails().get(1);
            if (right.getCondition() != null) {
                list.add(right.getIndex());
            }
            tree.put(node.getIndex(), list);
        }


        if (nodes.size() > 1) {
            BinaryExpression complexCondition;
           /* for (int i = nodes.size() - 2; i >= 0; i--) {
                Node node = nodes.get(i);
                if ((node.getListOfTails().get(1) != exit && node.getListOfTails().get(0).getCondition() == null) || (nodes.get(i + 1).getListOfTails().get(1) == exit && node.getListOfTails().get(0).getCondition() != null)) {
                    complexCondition = new BinaryExpression(OperationType.OR, node.getCondition(), nodes.get(i + 1).getCondition());
                    OperationType optype = ((BinaryExpression) (nodes.get(i + 1).getCondition())).getOperationType();
                    if (optype != OperationType.AND && optype != OperationType.OR && i != nodes.size() - 2) {
                        nodes.get(i + 1).setCondition(nodes.get(i + 1).getCondition().invert());
                    }
                } else {
                    complexCondition = new BinaryExpression(OperationType.AND, node.getCondition(), nodes.get(i + 1).getCondition());
                    OperationType optype = ((BinaryExpression) (node.getCondition())).getOperationType();
                    if (optype != OperationType.AND && optype != OperationType.OR) {
                        node.setCondition(node.getCondition().invert());
                    }
                }
                node.setCondition(complexCondition);
            }
            nodes.get(nodes.size() - 1).setCondition(nodes.get(nodes.size() - 1).getCondition().invert());
*/


        } else {
            start.setCondition(start.getCondition().invert());
        }
        return exit.getIndex();
    }

}

