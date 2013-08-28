package com.sdc.languages.java.printers;

import com.sdc.languages.general.printers.AbstractOperationPrinter;

public class JavaOperationPrinter extends AbstractOperationPrinter {
    protected static AbstractOperationPrinter ourInstance = new JavaOperationPrinter();

    public static AbstractOperationPrinter getInstance(){
        return ourInstance;
    }
}
