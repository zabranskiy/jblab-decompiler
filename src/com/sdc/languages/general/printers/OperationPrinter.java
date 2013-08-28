package com.sdc.languages.general.printers;

import com.sdc.ast.ExpressionType;

import static com.sdc.ast.ExpressionType.*;

public abstract class OperationPrinter {
    protected static OperationPrinter ourInstance;

    public static OperationPrinter getInstance() {
        return ourInstance;
    }

    protected OperationPrinter() {
    }

    public String getAddView() {
        return "+ ";
    }

    public String getSubView() {
        return "- ";
    }

    public String getDivView() {
        return "/ ";
    }

    public String getMulView() {
        return "* ";
    }

    public String getRemView() {
        return "% ";
    }

    public String getAndView() {
        return "&& ";
    }

    public String getOrView() {
        return "|| ";
    }

    public String getEqualityView() {
        return "== ";
    }

    public String getNotEqualityView() {
        return "!= ";
    }

    public String getGEView() {
        return ">= ";
    }

    public String getGreaterView() {
        return "> ";
    }

    public String getLEView() {
        return "<= ";
    }

    public String getLessView() {
        return "< ";
    }

    public String getSHLView() {
        return "<< ";
    }

    public String getSHLIncView() {
        return "<<= ";
    }

    public String getSHRView() {
        return ">> ";
    }

    public String getSHRIncView() {
        return ">>= ";
    }

    public String getBitwiseXorView() {
        return "^ ";
    }

    public String getBitwiseXorIncView() {
        return "^= ";
    }

    public String getBitwiseOrView() {
        return "| ";
    }

    public String getBitwiseOrIncView() {
        return "|= ";
    }

    public String getBitwiseAndView() {
        return "& ";
    }

    public String getBitwiseAndIncView() {
        return "&= ";
    }

    public String getUSHRView() {
        return ">>> ";
    }

    public String getUSHRIncView() {
        return ">>>= ";
    }

    public String getAddIncView() {
        return "+= ";
    }

    public String getSubIncView() {
        return "-= ";
    }

    public String getDivIncView() {
        return "/= ";
    }

    public String getMulIncView() {
        return "*= ";
    }

    public String getRemIncView() {
        return "%= ";
    }

    public String getIncView() {
        return "++";
    }

    public String getIncRevView() {
        return "++";
    }

    public String getDecView() {
        return "--";
    }

    public String getDecRevView() {
        return "--";
    }

    public String getNotView() {
        return "!";
    }

    public String getNegateView() {
        return "-";
    }

    public String getDoubleCastView() {
        return "(double) ";
    }

    public String getIntCastView() {
        return "(int) ";
    }

    public String getLongCastView() {
        return "(long) ";
    }

    public String getFloatCastView() {
        return "(float) ";
    }

    public String getCharCastView() {
        return "(char) ";
    }

    public String getShortCastView() {
        return "(short) ";
    }

    public String getByteCastView() {
        return "(byte) ";
    }

    public String getCheckCast(String myParam) {
        return ("(" + myParam + ") ");
    }

    public String getArrayLengthView() {
        return ".length";
    }


    public int getPriority(ExpressionType type) {
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


    public String getBooleanTypeView() {
        return "boolean";
    }

    public String getIntTypeView() {
        return "int";
    }

    public String getCharTypeView() {
        return "char";
    }

    public String getShortTypeView() {
        return "short";
    }

    public String getDoubleTypeView() {
        return "double";
    }

    public String getFloatTypeView() {
        return "float";
    }

    public String getByteTypeView() {
        return "byte";
    }

    public String getLongTypeView() {
        return "long";
    }

    public String getNotPrimitiveView(String className) {
        return className;
    }


    public String getBracketsView() {
        return "[]";
    }

    public String getTypeWithBracketsView(String type) {
        return type + "[]";
    }

    public String getSpaceAfterType() {
        return " ";
    }
}
