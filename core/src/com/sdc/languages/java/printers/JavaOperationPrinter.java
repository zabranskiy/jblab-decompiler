package com.sdc.languages.java.printers;

import com.sdc.languages.general.printers.OperationPrinter;

import org.jetbrains.annotations.NotNull;


public class JavaOperationPrinter extends OperationPrinter {
    protected static OperationPrinter ourInstance = new JavaOperationPrinter();

    @NotNull
    public static OperationPrinter getInstance() {
        return ourInstance;
    }
}
