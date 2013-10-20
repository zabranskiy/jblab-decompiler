package com.sdc.cfg.nodes;

import org.jetbrains.annotations.NotNull;


public class ExceptionHandler extends Node {
    private final String exceptionType;

    public ExceptionHandler(final @NotNull String type) {
        this.exceptionType = type;
    }
}
