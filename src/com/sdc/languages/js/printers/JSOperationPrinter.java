package com.sdc.languages.js.printers;

import com.sdc.languages.general.printers.OperationPrinter;

public class JSOperationPrinter extends OperationPrinter {
    protected static OperationPrinter ourInstance = new JSOperationPrinter();

    public static OperationPrinter getInstance(){
        return ourInstance;
    }
}
