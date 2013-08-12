package com.sdc.cfg.constructions;

import com.sdc.ast.controlflow.Statement;

import java.util.ArrayList;
import java.util.List;

public class ElementaryBlock extends Construction {
    private List<Statement> myStatements = new ArrayList<Statement>();

    public List<Statement> getStatements() {
        return myStatements;
    }

    public void setStatements(final List<Statement> statements) {
        this.myStatements = statements;
    }

    public Statement getLastStatement() {
        if (myStatements.isEmpty()) {
            return myStatements.get(myStatements.size() - 1);
        }
        return null;
    }

    public void addStatement(final Statement statement) {
        myStatements.add(statement);
    }
}
