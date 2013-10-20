package com.sdc.languages.general.printers;

import com.sdc.ast.ExpressionType;

import org.jetbrains.annotations.NotNull;

import static com.sdc.ast.ExpressionType.*;


public abstract class OperationPrinter {
    protected static OperationPrinter ourInstance;

    @NotNull
    public static OperationPrinter getInstance() {
        return ourInstance;
    }

    protected OperationPrinter() {
    }

    @NotNull
    public String getAddView() {
        return "+ ";
    }

    @NotNull
    public String getSubView() {
        return "- ";
    }

    @NotNull
    public String getDivView() {
        return "/ ";
    }

    @NotNull
    public String getMulView() {
        return "* ";
    }

    @NotNull
    public String getRemView() {
        return "% ";
    }

    @NotNull
    public String getAndView() {
        return "&& ";
    }

    @NotNull
    public String getOrView() {
        return "|| ";
    }

    @NotNull
    public String getEqualityView() {
        return "== ";
    }

    @NotNull
    public String getNotEqualityView() {
        return "!= ";
    }

    @NotNull
    public String getGEView() {
        return ">= ";
    }

    @NotNull
    public String getGreaterView() {
        return "> ";
    }

    @NotNull
    public String getLEView() {
        return "<= ";
    }

    @NotNull
    public String getLessView() {
        return "< ";
    }

    @NotNull
    public String getSHLView() {
        return "<< ";
    }

    @NotNull
    public String getSHLIncView() {
        return "<<= ";
    }

    @NotNull
    public String getSHRView() {
        return ">> ";
    }

    @NotNull
    public String getSHRIncView() {
        return ">>= ";
    }

    @NotNull
    public String getBitwiseXorView() {
        return "^ ";
    }

    @NotNull
    public String getBitwiseXorIncView() {
        return "^= ";
    }

    @NotNull
    public String getBitwiseOrView() {
        return "| ";
    }

    @NotNull
    public String getBitwiseOrIncView() {
        return "|= ";
    }

    @NotNull
    public String getBitwiseAndView() {
        return "& ";
    }

    @NotNull
    public String getBitwiseAndIncView() {
        return "&= ";
    }

    @NotNull
    public String getUSHRView() {
        return ">>> ";
    }

    @NotNull
    public String getUSHRIncView() {
        return ">>>= ";
    }

    @NotNull
    public String getAddIncView() {
        return "+= ";
    }

    @NotNull
    public String getSubIncView() {
        return "-= ";
    }

    @NotNull
    public String getDivIncView() {
        return "/= ";
    }

    @NotNull
    public String getMulIncView() {
        return "*= ";
    }

    @NotNull
    public String getRemIncView() {
        return "%= ";
    }

    @NotNull
    public String getIncView() {
        return "++";
    }

    @NotNull
    public String getIncRevView() {
        return "++";
    }

    @NotNull
    public String getDecView() {
        return "--";
    }

    @NotNull
    public String getDecRevView() {
        return "--";
    }

    @NotNull
    public String getNotView() {
        return "!";
    }

    @NotNull
    public String getNegateView() {
        return "-";
    }

    @NotNull
    public String getDoubleCastView() {
        return "(double) ";
    }

    @NotNull
    public String getIntCastView() {
        return "(int) ";
    }

    @NotNull
    public String getLongCastView() {
        return "(long) ";
    }

    @NotNull
    public String getFloatCastView() {
        return "(float) ";
    }

    @NotNull
    public String getCharCastView() {
        return "(char) ";
    }

    @NotNull
    public String getShortCastView() {
        return "(short) ";
    }

    @NotNull
    public String getByteCastView() {
        return "(byte) ";
    }

    @NotNull
    public String getCheckCast(String myParam) {
        return ("(" + myParam + ") ");
    }

    @NotNull
    public String getArrayLengthView() {
        return ".length";
    }

    public int getPriority(final @NotNull ExpressionType type) {
        if (type == VARIABLE || type == FIELD || type == CONST || type == NEWARRAY || type == NEW) {
            return 0;
        } else if (type == SQUARE_BRACKETS || type == ARRAYLENGTH || type == INVOCATION) {
            return 1;
        } else if (type == NOT || type == NEGATE || type == DEC || type == INC || type == DEC_REV || type == INC_REV
                || type == INT_CAST || type == DOUBLE_CAST || type == LONG_CAST || type == SHORT_CAST
                || type == BYTE_CAST || type == CHAR_CAST || type == FLOAT_CAST || type == CHECK_CAST) {
            return 2;
        } else if (type == MUL || type == DIV || type == REM) {
            return 3;
        } else if (type == SUB || type == ADD) {
            return 4;
        } else if (type == SHL || type == SHR || type == USHR) {
            return 5;
        } else if (type == GE || type == LE || type == LT || type == GT || type == INSTANCEOF) {
            return 6;
        } else if (type == EQ || type == NE) {
            return 7;
        } else if (type == BITWISE_AND) {
            return 8;
        } else if (type == BITWISE_XOR) {
            return 9;
        } else if (type == BITWISE_OR) {
            return 10;
        } else if (type == AND) {
            return 11;
        } else if (type == OR) {
            return 12;
        } else if (type == TERNARY_IF) {
            return 13;
        } else if (type == MUL_INC || type == DIV_INC || type == REM_INC || type == ADD_INC || type == SUB_INC ||
                type == BITWISE_OR_INC || type == BITWISE_AND_INC || type == BITWISE_XOR_INC ||
                type == SHL_INC || type == SHR_INC || type == USHR_INC) {
            return 14;
        }
        return 100;
    }

    @NotNull
    public String getBooleanTypeView() {
        return "boolean";
    }

    @NotNull
    public String getIntTypeView() {
        return "int";
    }

    @NotNull
    public String getCharTypeView() {
        return "char";
    }

    @NotNull
    public String getShortTypeView() {
        return "short";
    }

    @NotNull
    public String getDoubleTypeView() {
        return "double";
    }

    @NotNull
    public String getFloatTypeView() {
        return "float";
    }

    @NotNull
    public String getByteTypeView() {
        return "byte";
    }

    @NotNull
    public String getLongTypeView() {
        return "long";
    }

    @NotNull
    public String getNotPrimitiveView(String className) {
        return className;
    }

    @NotNull
    public String getBracketsView() {
        return "[]";
    }

    @NotNull
    public String getTypeWithBracketsView(String type) {
        return type + "[]";
    }

    @NotNull
    public String getSpaceAfterType() {
        return " ";
    }
}
