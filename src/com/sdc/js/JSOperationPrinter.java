package com.sdc.js;

import com.sdc.abstractLanguage.AbstractOperationPrinter;

public class JSOperationPrinter extends AbstractOperationPrinter {
    protected static AbstractOperationPrinter ourInstance = new JSOperationPrinter();

    public static AbstractOperationPrinter getInstance(){
        return ourInstance;
    }
}
