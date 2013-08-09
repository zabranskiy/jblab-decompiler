package com.sdc.abstractLanguage;

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

    public String getBitewiseAndView() {
        return "&& ";
    }

    public String getBitewseOrView() {
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

    public String getBitwiseAndView(){
        return "& ";
    }

    public String getUSHRView() {
        return ">>> ";
    }

}
