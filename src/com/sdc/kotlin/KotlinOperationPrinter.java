package com.sdc.kotlin;

import com.sdc.abstractLanguage.AbstractOperationPrinter;

public class KotlinOperationPrinter extends AbstractOperationPrinter {
    protected static AbstractOperationPrinter ourInstance = new KotlinOperationPrinter();

    public static AbstractOperationPrinter getInstance(){
        return ourInstance;
    }

    @Override
    public String getDoubleCastView() {
        return " as Double";
    }

    @Override
    public String getIntCastView() {
        return " as Int";
    }

    @Override
    public String getLongCastView() {
        return " as Long";
    }

    @Override
    public String getFloatCastView() {
        return " as Float";
    }

    @Override
    public String getCharCastView() {
        return " as Char";
    }

    @Override
    public String getShortCastView() {
        return " as Short";
    }

    @Override
    public String getByteCastView() {
        return " as Byte";
    }

    @Override
    public String getCheckCast(final String myParam) {
        return " as " + myParam;
    }
}
