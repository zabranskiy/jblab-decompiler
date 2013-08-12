package com.sdc.cfg.nodes;

import com.sdc.cfg.nodes.Node;

public class ExceptionHandler extends Node {
    private String exceptionType;

    public ExceptionHandler(String type) {
        this.exceptionType = type;
    }
}
