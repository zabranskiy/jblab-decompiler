package com.sdc.cfg.nodes;

import com.sdc.ast.controlflow.Return;
import com.sdc.ast.controlflow.Statement;
import com.sdc.ast.expressions.Expression;
import com.sdc.cfg.constructions.Construction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Label;

import java.util.ArrayList;
import java.util.List;


public class Node {
    protected List<Statement> myStatements = new ArrayList<Statement>();
    protected Expression myCondition;
    protected List<Label> myInnerLabels;
    protected Node myNextNode;
    protected int myIndex;
    protected Construction myOuterConstruction;
    protected List<Node> myNodeTails = new ArrayList<Node>();
    protected List<Node> myAncestors = new ArrayList<Node>(); // Fathers of a vertex

    protected boolean myIsEmpty = false;
    protected boolean myIsCaseEndNode = false;

    public Node() {
    }

    public Node(final @NotNull List<Statement> myStatements, final @NotNull List<Label> myInnerLabels, final int index) {
        this.myStatements = myStatements;
        this.myInnerLabels = myInnerLabels;
        this.myIndex = index;
    }

    public void addTail(final @NotNull Node node) {
        myNodeTails.add(node);
    }

    public void addAncestor(final @NotNull Node node) {
        myAncestors.add(node);
    }

    @NotNull
    public List<Node> getListOfTails() {
        return myNodeTails;
    }

    @NotNull
    public List<Node> getAncestors() {
        return myAncestors;
    }

    @NotNull
    public List<Statement> getStatements() {
        return myStatements;
    }

    public void setStatements(final @NotNull List<Statement> myStatements) {
        this.myStatements = myStatements;
    }

    public boolean statementsIsEmpty() {
        return myStatements.isEmpty();
    }

    @Nullable
    public Expression getCondition() {
        return myCondition;
    }

    public void setCondition(final @NotNull Expression condition) {
        this.myCondition = condition;
    }

    @Nullable
    public List<Label> getInnerLabels() {
        return myInnerLabels;
    }

    public void setInnerLabels(final @NotNull List<Label> myInnerLabels) {
        this.myInnerLabels = myInnerLabels;
    }

    public boolean containsLabel(final @NotNull Label label) {
        return myInnerLabels.contains(label);
    }

    @Nullable
    public Node getNextNode() {
        return myNextNode;
    }

    public void setNextNode(final @Nullable Node nextNode) {
        this.myNextNode = nextNode;
    }

    @Nullable
    public Construction getOuterConstruction() {
        return myOuterConstruction;
    }

    public void setOuterConstruction(final @NotNull Construction outerConstruction) {
        this.myOuterConstruction = outerConstruction;
    }

    public int getIndex() {
        return myIndex;
    }

    public boolean isEmpty() {
        return myIsEmpty;
    }

    public void setIsEmpty(final boolean isEmpty) {
        this.myIsEmpty = isEmpty;
    }

    public boolean isCaseEndNode() {
        return myIsCaseEndNode;
    }

    public void setIsCaseEndNode(final boolean isCaseEndNode) {
        this.myIsCaseEndNode = myIsCaseEndNode || isCaseEndNode;
    }

    public boolean isLastStatementReturn() {
        return (myStatements.size() != 0) && (myStatements.get(myStatements.size() - 1) instanceof Return);
    }

    public void removeChild(final @NotNull Node child) {
        myNodeTails.remove(child);
    }

    public void removeAncestor(final @NotNull Node ancestor) {
        myAncestors.remove(ancestor);
    }
}