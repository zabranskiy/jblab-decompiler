package com.sdc.cfg.constructions;

import com.sdc.ast.controlflow.Statement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class ElementaryBlock extends Construction {
    private List<Statement> myStatements = new ArrayList<Statement>();

    @NotNull
    public List<Statement> getStatements() {
        return myStatements;
    }

    public void setStatements(final @NotNull List<Statement> statements) {
        this.myStatements = statements;
    }

    @Nullable
    public Statement getFirstStatement() {
        if (!myStatements.isEmpty()) {
            return myStatements.get(0);
        }
        return null;
    }

    @Nullable
    public Statement getBeforeLastStatement() {
        if (myStatements.size() >= 2) {
            return myStatements.get(myStatements.size() - 2);
        }
        return null;
    }

    @Nullable
    public Statement getLastStatement() {
        if (!myStatements.isEmpty()) {
            return myStatements.get(myStatements.size() - 1);
        }
        return null;
    }

    public void removeFirstStatement() {
        if (!myStatements.isEmpty()) {
            myStatements.remove(0);
        }
    }

    public void removeLastStatement() {
        if (!myStatements.isEmpty()) {
            myStatements.remove(myStatements.size() - 1);
        }
    }

    public void addStatement(final @NotNull Statement statement) {
        myStatements.add(statement);
    }
}
