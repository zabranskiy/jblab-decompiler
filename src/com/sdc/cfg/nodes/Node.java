package com.sdc.cfg.nodes;

import com.sdc.ast.controlflow.Return;
import com.sdc.ast.controlflow.Statement;
import com.sdc.ast.expressions.Expression;
import org.objectweb.asm.Label;

import java.util.ArrayList;
import java.util.List;

public class Node {
    protected List<Statement> myStatements = new ArrayList<Statement>();
    protected Expression myCondition;
    protected List<Label> myInnerLabels;
    protected Node myNextNode;
    protected List<Node> myNodeTails = new ArrayList<Node>();
    protected List<Node> myAncestors = new ArrayList<Node>();  // Fathers of a vertex

    protected boolean isEmpty = false;
    protected boolean myIsCaseEndNode = false;

    public Node() {
    }

    public Node(List<Statement> myStatements, List<Label> myInnerLabels) {
        this.myStatements = myStatements;
        this.myInnerLabels = myInnerLabels;
    }

    public void addTail(Node node) {
        myNodeTails.add(node);
    }

    public void addAncestor(Node node) {
        myAncestors.add(node);
    }

    public List<Node> getListOfTails() {
        return myNodeTails;
    }

    public List<Node> getAncestors() {
        return myAncestors;
    }

    public List<Statement> getStatements() {
        return myStatements;
    }

    public boolean statementsIsEmpty() {
        return myStatements.isEmpty();
    }

    public void setStatements(List<Statement> myStatements) {
        this.myStatements = myStatements;
    }

    public void setCondition(Expression condition) {
        this.myCondition = condition;
    }

    public void setInnerLabels(List<Label> myInnerLabels) {
        this.myInnerLabels = myInnerLabels;
    }

    public boolean containsLabel(Label label) {
        return myInnerLabels.contains(label);
    }

    public Expression getCondition() {
        return myCondition;
    }

    public List<Label> getInnerLabels() {
        return myInnerLabels;
    }

    public boolean isLastStatementReturn() {
        return (myStatements.size() != 0) && (myStatements.get(myStatements.size() - 1) instanceof Return);
    }

    public Node getNextNode() {
        return myNextNode;
    }

    public void setNextNode(Node myNextNode) {
        this.myNextNode = myNextNode;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public void removeChild(final Node child) {
        myNodeTails.remove(child);
    }

    public void removeAncestor(final Node ancestor) {
        myAncestors.remove(ancestor);
    }

    public boolean isCaseEndNode() {
        return myIsCaseEndNode;
    }

    public void setIsCaseEndNode(final boolean isCaseEndNode) {
        this.myIsCaseEndNode = myIsCaseEndNode || isCaseEndNode;
    }
}
