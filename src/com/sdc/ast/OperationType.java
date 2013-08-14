package com.sdc.ast;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitrii.Pozdin
 * Date: 8/12/13
 * Time: 5:21 PM
 * To change this template use File | Settings | File Templates.
 */
public enum OperationType {
    ADD, SUB, MUL, DIV, REM,
    INC, DEC, INC_REV,DEC_REV, ADD_INC, SUB_INC, MUL_INC, DIV_INC, REM_INC,
    AND, OR, BITWISE_AND, BITWISE_OR, BITWISE_XOR, EQ, NE, GE, GT, LE, LT, SHR, SHL, USHR,
    NOT, NEGATE, DOUBLE_CAST, INT_CAST, LONG_CAST, SHORT_CAST, BYTE_CAST, CHAR_CAST, FLOAT_CAST, TERNARY_IF, CHECK_CAST
}
