package com.sdc.languages.js.printers;

import com.sdc.languages.general.printers.AbstractOperationPrinter;

public class JSOperationPrinter extends AbstractOperationPrinter {
    protected static AbstractOperationPrinter ourInstance = new JSOperationPrinter();

    public static AbstractOperationPrinter getInstance(){
        return ourInstance;
    }
}
