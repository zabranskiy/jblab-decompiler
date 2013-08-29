package com.sdc.languages.java.printers;

import com.sdc.languages.general.printers.OperationPrinter;

public class JavaOperationPrinter extends OperationPrinter {
    protected static OperationPrinter ourInstance = new JavaOperationPrinter();

    public static OperationPrinter getInstance(){
        return ourInstance;
    }
}
