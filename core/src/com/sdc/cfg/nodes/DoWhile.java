package com.sdc.cfg.nodes;

import com.sdc.ast.controlflow.Statement;

import org.objectweb.asm.Label;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class DoWhile extends Node {
    public DoWhile(final @NotNull List<Statement> statements,
                   final @NotNull List<Label> labels,
                   final int size) {
        super(statements, labels, size);
    }
}
