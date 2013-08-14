package com.sdc.abstractLanguage;

import com.sdc.ast.OperationType;

import static com.sdc.ast.OperationType.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitrii.Pozdin
 * Date: 8/8/13
 * Time: 1:49 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractOperationPrinter {
    protected static AbstractOperationPrinter ourInstance;

    public static AbstractOperationPrinter getInstance() {
        return ourInstance;
    }

    protected AbstractOperationPrinter() {
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

    public String getSHRView() {
        return ">> ";
    }

    public String getXorView() {
        return "^ ";
    }

    public String getBitwiseOrView() {
        return "| ";
    }

    public String getBitwiseAndView() {
        return "& ";
    }

    public String getUSHRView() {
        return ">>> ";
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

    public int getPriority(OperationType type) {
        if (type == NOT || type == NEGATE || type == DEC || type == INC ||type == DEC_REV || type == INC_REV
                || type == INT_CAST || type == DOUBLE_CAST || type == LONG_CAST || type == SHORT_CAST
                || type == BYTE_CAST || type == CHAR_CAST || type == FLOAT_CAST || type == CHECK_CAST) {
            return 1;
        } else if (type == MUL || type == DIV || type == REM) {
            return 2;
        } else if (type == SUB) {
            return 3;
        } else if (type == ADD) {
            return 4;
        } else if (type == SHL || type == SHR || type == USHR) {
            return 5;
        } else if (type == GE || type == LE || type == LT || type == GT) {
            return 6;
        } else if (type == EQ || type == NE) {
            return 7;
        } else if (type == MUL_INC || type == DIV_INC || type == REM_INC || type == ADD_INC || type == SUB_INC) {
            return 14;
        }

        switch (type) {
            case BITWISE_AND:
                return 8;
            case BITWISE_XOR:
                return 9;
            case BITWISE_OR:
                return 10;
            case AND:
                return 11;
            case OR:
                return 12;
            case TERNARY_IF:
                return 13;
            default:
                return 100;
        }

    }


}
