package com.sdc.languages.kotlin.printers;

import com.sdc.languages.general.printers.OperationPrinter;

import org.jetbrains.annotations.NotNull;


public class KotlinOperationPrinter extends OperationPrinter {
    protected static OperationPrinter ourInstance = new KotlinOperationPrinter();

    @NotNull
    public static OperationPrinter getInstance(){
        return ourInstance;
    }

    @NotNull
    @Override
    public String getDoubleCastView() {
        return ".toDouble()";
    }

    @NotNull
    @Override
    public String getIntCastView() {
        return ".toInt()";
    }

    @NotNull
    @Override
    public String getLongCastView() {
        return ".toLong()";
    }

    @NotNull
    @Override
    public String getFloatCastView() {
        return ".toFloat()";
    }

    @NotNull
    @Override
    public String getCharCastView() {
        return ".toChar()";
    }

    @NotNull
    @Override
    public String getShortCastView() {
        return ".toShort()";
    }

    @NotNull
    @Override
    public String getByteCastView() {
        return ".toByte()";
    }

    @NotNull
    @Override
    public String getCheckCast(final String myParam) {
        return " as " + myParam;
    }

    @NotNull
    @Override
    public String getTypeWithBracketsView(String type) {
        return "Array<"+type+">";
    }

    @NotNull
    @Override
    public String getBooleanTypeView() {
        return "Boolean";
    }

    @NotNull
    @Override
    public String getIntTypeView() {
        return "Int";
    }

    @NotNull
    @Override
    public String getCharTypeView() {
        return "Char";
    }

    @NotNull
    @Override
    public String getShortTypeView() {
        return "Short";
    }

    @NotNull
    @Override
    public String getDoubleTypeView() {
        return "Double";
    }

    @NotNull
    @Override
    public String getFloatTypeView() {
        return "Float";
    }

    @NotNull
    @Override
    public String getByteTypeView() {
        return "Byte";
    }

    @NotNull
    @Override
    public String getLongTypeView() {
        return "Long";
    }

    @NotNull
    @Override
    public String getNotPrimitiveView(String className) {
        return className;
    }

    @NotNull
    @Override
    public String getSpaceAfterType() {
        return "";
    }
}
