package com.sdc.cfg;

import com.sdc.cfg.nodes.Node;

public class ExceptionHandler extends Node {
    private String exceptionType;

    public ExceptionHandler(String type) {
        this.exceptionType = type;
    }
}
