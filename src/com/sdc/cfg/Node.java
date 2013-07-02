package com.sdc.cfg;

import com.sdc.ast.controlflow.Return;
import com.sdc.ast.controlflow.Statement;
import com.sdc.ast.expressions.Expression;
import org.objectweb.asm.Label;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Statement> myStatements = new ArrayList<Statement>();
    private Expression myCondition;
    private List<Label> myInnerLabels;
    private List<Node> myNodeTails = new ArrayList<Node>();
    private boolean isEmpty = false;

    public Node() {
    }

    public Node(List<Statement> myStatements, List<Label> myInnerLabels) {
        this.myStatements = myStatements;
        this.myInnerLabels = myInnerLabels;
    }

    public void addTail(Node node) {
        myNodeTails.add(node);
    }

    public List<Node> getListOfTails() {
        return myNodeTails;
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

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }
}
