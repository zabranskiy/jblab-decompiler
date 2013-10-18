package com.sdc.cfg.nodes;

import com.sdc.ast.controlflow.Statement;
import org.objectweb.asm.Label;

import java.util.ArrayList;

public class DoWhile extends Node {
    public DoWhile(ArrayList<Statement> statements, ArrayList<Label> labels, int size) {
        super(statements, labels, size);
    }
}
