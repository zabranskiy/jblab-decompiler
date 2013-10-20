package com.sdc.languages.general;

import com.sdc.ast.controlflow.Assignment;
import com.sdc.ast.controlflow.Increment;
import com.sdc.ast.controlflow.Statement;
import com.sdc.ast.expressions.ArrayLength;
import com.sdc.ast.expressions.InstanceInvocation;
import com.sdc.ast.expressions.UnaryExpression;
import com.sdc.ast.expressions.identifiers.Variable;

import com.sdc.cfg.constructions.*;
import com.sdc.cfg.nodes.DoWhile;
import com.sdc.cfg.nodes.Node;
import com.sdc.cfg.nodes.Switch;
import com.sdc.cfg.nodes.SwitchCase;

import com.sdc.util.DominatorTreeGenerator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class ConstructionBuilder {
    private final List<Node> myNodes;
    private final DominatorTreeGenerator gen;
    private final int size;
    private final int[] domi;

    public ConstructionBuilder(final @NotNull List<Node> myNodes, final @NotNull DominatorTreeGenerator gen) {
        this.myNodes = myNodes;
        this.gen = gen;
        this.domi = gen.getDomi();
        this.size = myNodes.size();
    }

    @NotNull
    protected ConstructionBuilder createConstructionBuilder(final @NotNull List<Node> myNodes,
                                                            final @NotNull DominatorTreeGenerator gen) {
        return new ConstructionBuilder(myNodes, gen);
    }

    @NotNull
    public Construction build() {
        return extractArrayForEach(extractIteratorForEach(extractFor(build(myNodes.get(0)))));
    }

    @NotNull
    protected Construction extractArrayForEach(final @NotNull Construction baseConstruction) {
        final Construction forStartConstruction = baseConstruction.getNextConstruction();

        if (baseConstruction instanceof ElementaryBlock && forStartConstruction != null && forStartConstruction instanceof For) {
            final ElementaryBlock blockBeforeForConstruction = (ElementaryBlock) baseConstruction;

            if (!blockBeforeForConstruction.getStatements().isEmpty()) {
                final Statement containerDeclarationForFor = blockBeforeForConstruction.getBeforeLastStatement();
                final Statement containerLengthDeclaration = blockBeforeForConstruction.getLastStatement();

                if (containerDeclarationForFor instanceof Assignment && containerLengthDeclaration instanceof Assignment) {
                    final Assignment containerAssignmentForFor = (Assignment) containerDeclarationForFor;
                    final Assignment containerLengthAssignment = (Assignment) containerLengthDeclaration;

                    if ((containerAssignmentForFor.getLeft().getType().isArray())
                            && containerLengthAssignment.getRight() instanceof ArrayLength) {
                        final List<Variable> forEachVariables = new ArrayList<Variable>();

                        final Statement firstStatement = ((ElementaryBlock) ((For) forStartConstruction).getBody()).getFirstStatement();
                        forEachVariables.add((Variable) ((Assignment) (firstStatement)).getLeft());

                        final ForEach forEach = new ForEach(forEachVariables, containerAssignmentForFor.getRight());

                        final Construction body = ((For) forStartConstruction).getBody();

                        forEach.setBody(body);

                        ((ElementaryBlock) body).removeFirstStatement();
                        blockBeforeForConstruction.removeLastStatement();
                        blockBeforeForConstruction.removeLastStatement();

                        forEach.setNextConstruction(forStartConstruction.getNextConstruction());
                        blockBeforeForConstruction.setNextConstruction(forEach);
                    }
                }
            }
        }

        return baseConstruction;
    }

    @NotNull
    private Construction extractIteratorForEach(final @NotNull Construction baseConstruction) {
        final Construction whileStartConstruction = baseConstruction.getNextConstruction();

        if (baseConstruction instanceof ElementaryBlock && whileStartConstruction != null && whileStartConstruction instanceof While) {
            final ElementaryBlock blockBeforeWhileConstruction = (ElementaryBlock) baseConstruction;

            if (!blockBeforeWhileConstruction.getStatements().isEmpty()) {
                final Statement variableDeclarationForWhile = blockBeforeWhileConstruction.getLastStatement();

                if (variableDeclarationForWhile instanceof Assignment) {
                    final Assignment variableAssignmentForWhen = (Assignment) variableDeclarationForWhile;

                    if (variableAssignmentForWhen.getLeft().getType()
                            .toString().toLowerCase().replace("?", "").trim().equals("iterator")
                            && ((While) whileStartConstruction).getCondition() instanceof UnaryExpression
                            && ((InstanceInvocation) ((UnaryExpression) ((While) whileStartConstruction).getCondition()).getOperand()).getFunction().equals("hasNext")) {

                        final List<Variable> forEachVariables = new ArrayList<Variable>();
                        final Statement firstStatement = ((ElementaryBlock) ((While) whileStartConstruction).getBody()).getFirstStatement();
                        forEachVariables.add((Variable) ((Assignment) firstStatement).getLeft());

                        final ForEach forEach = new ForEach(forEachVariables
                                , ((InstanceInvocation) variableAssignmentForWhen.getRight()).getInstance());

                        final Construction body = ((While) whileStartConstruction).getBody();

                        forEach.setBody(body);
                        ((ElementaryBlock) body).removeFirstStatement();
                        blockBeforeWhileConstruction.removeLastStatement();

                        forEach.setNextConstruction(whileStartConstruction.getNextConstruction());
                        blockBeforeWhileConstruction.setNextConstruction(forEach);
                    }
                }
            }
        }

        return baseConstruction;
    }

    @NotNull
    private Construction extractFor(final @NotNull Construction baseConstruction) {
        final Construction whileStartConstruction = baseConstruction.getNextConstruction();

        if (baseConstruction instanceof ElementaryBlock && whileStartConstruction != null && whileStartConstruction instanceof While) {
            final ElementaryBlock blockBeforeWhileConstruction = (ElementaryBlock) baseConstruction;

            if (!blockBeforeWhileConstruction.getStatements().isEmpty()) {
                final Statement variableDeclarationForWhile = blockBeforeWhileConstruction.getLastStatement();

                if (variableDeclarationForWhile instanceof Assignment) {
                    final Assignment variableAssignmentForWhen = (Assignment) variableDeclarationForWhile;

                    if (variableAssignmentForWhen.getLeft() instanceof Variable) {
                        final int forVariableIndex = ((Variable) variableAssignmentForWhen.getLeft()).getIndex();
                        Construction currentConstruction = ((While) whileStartConstruction).getBody();

                        while (currentConstruction.getNextConstruction() != null) {
                            currentConstruction = currentConstruction.getNextConstruction();
                        }

                        if (currentConstruction instanceof ElementaryBlock) {
                            final ElementaryBlock forAfterThoughtBlock = (ElementaryBlock) currentConstruction;
                            final Statement forAfterThought = forAfterThoughtBlock.getLastStatement();

                            if (forAfterThought instanceof Assignment) {
                                final Assignment afterThoughtAssignment = (Assignment) forAfterThought;

                                if (afterThoughtAssignment.getLeft() instanceof Variable) {
                                    final int afterThoughtVariableIndex = ((Variable) afterThoughtAssignment.getLeft()).getIndex();
                                    if (afterThoughtVariableIndex == forVariableIndex) {
                                        final For forConstruction = new For(variableAssignmentForWhen
                                                , ((While) whileStartConstruction).getCondition(), afterThoughtAssignment);
                                        forConstruction.setBody(((While) whileStartConstruction).getBody());

                                        blockBeforeWhileConstruction.removeLastStatement();
                                        blockBeforeWhileConstruction.setNextConstruction(forConstruction);
                                        forConstruction.setNextConstruction(whileStartConstruction.getNextConstruction());
                                        forAfterThoughtBlock.removeLastStatement();

                                        return blockBeforeWhileConstruction;
                                    }
                                }
                            } else if (forAfterThought instanceof Increment) {
                                final Increment afterThoughtIncrement = (Increment) forAfterThought;
                                final int afterThoughtVariableIndex = afterThoughtIncrement.getVariable().getIndex();

                                if (afterThoughtVariableIndex == forVariableIndex) {
                                    final For forConstruction = new For(variableAssignmentForWhen
                                            , ((While) whileStartConstruction).getCondition(), afterThoughtIncrement);
                                    forConstruction.setBody(((While) whileStartConstruction).getBody());

                                    blockBeforeWhileConstruction.removeLastStatement();
                                    blockBeforeWhileConstruction.setNextConstruction(forConstruction);
                                    forConstruction.setNextConstruction(whileStartConstruction.getNextConstruction());
                                    forAfterThoughtBlock.removeLastStatement();

                                    return blockBeforeWhileConstruction;
                                }
                            }
                        }
                    }
                }

            }
        }

        return baseConstruction;
    }

    @NotNull
    private Construction build(final @NotNull Node node) {
        final Node doWhileNode = checkForDoWhileLoop(node);

        if (doWhileNode == null) {
            final Construction elementaryBlock = extractElementaryBlock(node);
            final Construction currentConstruction = extractConstruction(node);

            if (node.getCondition() == null && !(node instanceof Switch)) {
                node.setOuterConstruction(elementaryBlock);
            } else {
                node.setOuterConstruction(currentConstruction);
            }

            if (currentConstruction == null && !node.getListOfTails().isEmpty()) {
                final Node myNextNode = node.getListOfTails().get(0);
                if (checkForIndexOutOfBound(myNextNode)) {
                    node.setNextNode(myNextNode);
                    extractNextConstruction(elementaryBlock, node);
                    return elementaryBlock;
                }
            }

            elementaryBlock.setNextConstruction(currentConstruction);
            return elementaryBlock;
        } else {
            return extractDoWhile(node, doWhileNode);
        }
    }

    private boolean checkForIndexOutOfBound(final @NotNull Node node) {
        return getRelativeIndex(node.getIndex()) < size && getRelativeIndex(node.getIndex()) >= 0;
    }

    private Construction extractConstruction(final @NotNull Node node) {
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

    @Nullable
    private Node checkForDoWhileLoop(final @NotNull Node node) {
        for (int i = node.getAncestors().size() - 1; i >= 0; i--) {
            Node ancestor = node.getAncestors().get(i);
            if (ancestor instanceof DoWhile && node.getIndex() < ancestor.getIndex() && checkForIndexOutOfBound(ancestor)) {
                return ancestor;
            }
        }
        return null;
    }

    @NotNull
    private Construction extractDoWhile(final @NotNull Node begin, final @NotNull Node node) {
        final ElementaryBlock elementaryBlock = new ElementaryBlock();
        elementaryBlock.setStatements(node.getStatements());

        final com.sdc.cfg.constructions.DoWhile doWhileConstruction = new com.sdc.cfg.constructions.DoWhile(node.getCondition());

        begin.removeAncestor(node);

        final int leftBound = getRelativeIndex(begin.getIndex());
        final int rightBound = getRelativeIndex(node.getIndex());
        doWhileConstruction.setBody(createConstructionBuilder(myNodes.subList(leftBound, rightBound), gen).build());

        final int nextNodeIndex = node.getIndex() + 1;
        if (nextNodeIndex <= myNodes.get(size - 1).getIndex()) {
            node.setNextNode(myNodes.get(getRelativeIndex(nextNodeIndex)));
        }

        elementaryBlock.setNextConstruction(doWhileConstruction);

        if (node.getNextNode() != null) {
            extractNextConstruction(doWhileConstruction, node);
        }

        return elementaryBlock;
    }

    @NotNull
    private Construction extractElementaryBlock(final @NotNull Node node) {
        final ElementaryBlock elementaryBlock = new ElementaryBlock();
        elementaryBlock.setStatements(node.getStatements());
        return elementaryBlock;
    }

    @Nullable
    private Construction extractSwitch(final @NotNull Node node) {
        if (node instanceof Switch) {
            final Switch switchNode = (Switch) node;
            final com.sdc.cfg.constructions.Switch switchConstruction = new com.sdc.cfg.constructions.Switch(switchNode.getExpr());

            Node nextNode = findNextNodeToSwitchWithDefaultCase(switchNode);

            if (!switchNode.hasRealDefaultCase()) {
                switchNode.removeFakeDefaultCase();
                nextNode = findNextNodeToSwitchWithoutDefaultCase(switchNode);
            }

            final List<SwitchCase> switchCases = switchNode.getCases();

            for (int i = 0; i < switchCases.size(); i++) {
                final int leftBound = getRelativeIndex(switchCases.get(i).getCaseBody().getIndex());
                final int rightBound = i != switchCases.size() - 1
                        ? getRelativeIndex(switchCases.get(i + 1).getCaseBody().getIndex())
                        : nextNode == null ? size : getRelativeIndex(nextNode.getIndex());

                final Construction caseBody = createConstructionBuilder(myNodes.subList(leftBound, rightBound), gen).build();

                com.sdc.cfg.constructions.SwitchCase switchCase = new com.sdc.cfg.constructions.SwitchCase(caseBody);
                switchCase.setKeys(switchCases.get(i).getKeys());

                switchConstruction.addCase(switchCase);
            }

            if (nextNode == null) {
                addBreakToAllOutgoingLinks();
            } else {
                addBreakToAncestors(nextNode);
            }

            if (nextNode != null && checkForIndexOutOfBound(nextNode)) {
                switchNode.setNextNode(nextNode);
                extractNextConstruction(switchConstruction, switchNode);
            }

            return switchConstruction;
        }
        return null;
    }

    @Nullable
    private Node findNextNode(final @NotNull Node node) {
        Node result = null;

        for (int i = 0; i < domi.length; i++) {
            if (domi[i] == node.getIndex()) {
                boolean isTail = false;
                for (final Node tail : node.getListOfTails()) {
                    if (i == tail.getIndex()) {
                        isTail = true;
                        break;
                    }
                }
                if (!isTail) {
                    if (getRelativeIndex(i) >= 0 && getRelativeIndex(i) < size) {
                        result = myNodes.get(getRelativeIndex(i));
                    }
                    break;
                }
            }
        }

        return result;
    }

    @Nullable
    private Node findNextNodeToSwitchWithDefaultCase(final @NotNull Switch switchNode) {
        return findNextNode(switchNode);
    }

    @Nullable
    private Node findNextNodeToSwitchWithoutDefaultCase(final @NotNull Switch switchNode) {
        final Node defaultBranch = switchNode.getNodeByKeyIndex(-1);

        switchNode.removeChild(defaultBranch);
        defaultBranch.removeAncestor(switchNode);

        return defaultBranch;
    }

    @Nullable
    private Construction extractConditionBlock(final @NotNull Node node) {
        if (node.getCondition() != null) {
            for (final Node ancestor : node.getAncestors()) {
                if (node.getIndex() < ancestor.getIndex()) {
                    if (domi[node.getIndex()] != domi[node.getListOfTails().get(1).getIndex()] && checkForIndexOutOfBound(node)) {
                        node.setNextNode(node.getListOfTails().get(1));
                    }
                    final com.sdc.cfg.constructions.While whileConstruction = new com.sdc.cfg.constructions.While(node.getCondition());


                    final int relativeIndexOfLeftTail = getRelativeIndex(node.getListOfTails().get(0));
                    final int relativeIndexOfLoop = getRelativeIndex(gen.getRightIndexForLoop(node.getIndex()));
                    final List<Node> whileBody = myNodes.subList(relativeIndexOfLeftTail
                            , relativeIndexOfLeftTail > relativeIndexOfLoop
                            ? getRelativeIndex(node.getListOfTails().get(1))
                            : relativeIndexOfLoop);

                    whileConstruction.setBody(createConstructionBuilder(whileBody, gen).build());
                    if (node.getNextNode() != null && checkForIndexOutOfBound(node.getNextNode())) {
                        extractNextConstruction(whileConstruction, node);
                    }

                    placeBreakAndContinue(node, whileBody);
                    removeBreakAndContinueFromLastConstruction(whileConstruction.getBody());

                    return whileConstruction;
                }
            }

            /// IF
            final com.sdc.cfg.constructions.ConditionalBlock conditionalBlock = new ConditionalBlock(node.getCondition());

            boolean fl = false;
            for (final Node ancestor : node.getAncestors()) {
                if (ancestor.getIndex() > node.getIndex()) {
                    fl = true;
                    break;
                }
            }

            if (!fl) {
                final Node nextNode = findNextNode(node);
                node.setNextNode(nextNode);

                final Node leftNode = node.getListOfTails().get(0);
                final Node rightNode = node.getListOfTails().get(1);
                final int rightIndex = getRelativeIndex(rightNode);

                if (node.getNextNode() == null) {
                    if (hasNotElse(rightNode) || checkRightTail(node)) {
                        if (rightNode.getIndex() <= myNodes.get(size - 1).getIndex()) {
                            node.setNextNode(checkForIndexOutOfBound(rightNode) ? rightNode : null);
                        }
                        conditionalBlock.setThenBlock(
                                createConstructionBuilder(myNodes.subList(
                                        getRelativeIndex(leftNode)
                                        , checkForIndexOutOfBound(rightNode) ? rightIndex : size), gen).build());
                    } else {
                        conditionalBlock.setThenBlock(
                                createConstructionBuilder(myNodes.subList(
                                        getRelativeIndex(leftNode), rightIndex), gen).build());
                        if (rightIndex < size) {
                            final List<Node> elseBody = myNodes.subList(rightIndex, size);

                            if (elseBody.size() > 1 || !elseBody.get(0).getStatements().isEmpty()) {
                                conditionalBlock.setElseBlock(
                                        createConstructionBuilder(myNodes.subList(rightIndex, size), gen).build());
                            }
                        } else {
                            final ElementaryBlock block = new ElementaryBlock();
                            block.setBreak("");
                            conditionalBlock.setElseBlock(block);
                            node.getAncestors().get(0).setNextNode(node.getListOfTails().get(1));
                        }
                    }
                } else {
                    conditionalBlock.setThenBlock(
                            createConstructionBuilder(myNodes.subList(
                                    getRelativeIndex(leftNode), rightIndex), gen).build());
                    conditionalBlock.setElseBlock(
                            createConstructionBuilder(myNodes.subList(
                                    rightIndex, getRelativeIndex(node.getNextNode())), gen).build());
                }
            }
            // TODO: test second condition for switch with if in a case
            if (node.getNextNode() != null && checkForIndexOutOfBound(node.getNextNode())) {
                extractNextConstruction(conditionalBlock, node);
            }
            return conditionalBlock;
        }
        return null;
    }

    private void placeBreakAndContinue(final @NotNull Node begin, final @NotNull List<Node> nodes) {
        final int leftBound = nodes.get(0).getIndex();
        final int rightBound = nodes.get(nodes.size() - 1).getIndex();
        final int beginIndex = begin.getIndex();

        for (final Node node : nodes) {
            for (final Node tail : node.getListOfTails()) {
                final int tailIndex = tail.getIndex();

                if (tailIndex != beginIndex && (tailIndex < leftBound || tailIndex > rightBound)) {
                    node.getOuterConstruction().setBreak("");
                    if (node.getOuterConstruction().hasContinue()) {
                        node.getOuterConstruction().setContinue(null);
                    }
                }

                if (tailIndex == beginIndex && !node.getOuterConstruction().hasBreak()) {
                    node.getOuterConstruction().setContinue("");
                }
            }
        }
    }

    private void removeBreakAndContinueFromLastConstruction(@NotNull Construction start) {
        while (start.getNextConstruction() != null) {
            start = start.getNextConstruction();
        }

        start.setBreak(null);
        start.setContinue(null);

        if (start instanceof ConditionalBlock) {
            final ConditionalBlock conditionalBlock = (ConditionalBlock) start;
            if (conditionalBlock.getElseBlock() != null && conditionalBlock.getThenBlock().hasBreak()) {
                removeBreakAndContinueFromLastConstruction(conditionalBlock.getElseBlock());
            }
        }
    }

    private boolean hasNotElse(final @NotNull Node node) {
        int count = 0;
        // ????
        for (final Node ancestor : node.getAncestors()) {
            if (node.getIndex() > ancestor.getIndex()) {
                count++;
            }
        }

        return count > 1;
    }

    private boolean checkRightTail(final @NotNull Node node) {
        return node.getListOfTails().get(1).getIndex() < node.getIndex();
    }

    private int getRelativeIndex(final @NotNull Node node) {
        return getRelativeIndex(node.getIndex());
    }

    private int getRelativeIndex(final int index) {
        return index - myNodes.get(0).getIndex();
    }

    private void extractNextConstruction(final @NotNull Construction construction, final @NotNull Node currentNode) {
        final int leftBound = getRelativeIndex(currentNode.getNextNode());

        construction.setNextConstruction(createConstructionBuilder(myNodes.subList(leftBound, size), gen).build());
    }

    private void addBreakToAncestors(final @NotNull Node child) {
        for (final Node parent : child.getAncestors()) {
            if (parent.getOuterConstruction() != null) {
                parent.getOuterConstruction().setBreak("");
            }
        }
    }

    private void addBreakToAllOutgoingLinks() {
        final int firstNodeIndex = myNodes.get(0).getIndex();
        final int lastNodeIndex = myNodes.get(size - 1).getIndex();

        for (final Node node : myNodes) {
            for (final Node tail : node.getListOfTails()) {
                final int tailIndex = tail.getIndex();
                if ((tailIndex < firstNodeIndex || tail.getIndex() > lastNodeIndex) && node.getOuterConstruction() != null) {
                    node.getOuterConstruction().setBreak("");
                }
            }
        }
    }
}
