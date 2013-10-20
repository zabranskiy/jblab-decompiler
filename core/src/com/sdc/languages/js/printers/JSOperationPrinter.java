package com.sdc.languages.js.printers;

import com.sdc.languages.general.printers.OperationPrinter;

import org.jetbrains.annotations.NotNull;


public class JSOperationPrinter extends OperationPrinter {
    protected static OperationPrinter ourInstance = new JSOperationPrinter();

    @NotNull
    public static OperationPrinter getInstance() {
        return ourInstance;
    }
}
