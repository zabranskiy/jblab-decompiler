package com.sdc.java;

import com.sdc.abstractLanguage.AbstractOperationPrinter;

public class JavaOperationPrinter extends AbstractOperationPrinter {
    protected static AbstractOperationPrinter ourInstance = new JavaOperationPrinter();

    public static AbstractOperationPrinter getInstance(){
        return ourInstance;
    }
}
