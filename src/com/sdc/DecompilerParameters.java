package com.sdc;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class DecompilerParameters {
    @Parameter(description = "Prints a disassembled view of the given class using specified language.")
    private List<String> myParameters = new ArrayList<String>();

    @Parameter(names = {"-l", "--language"}, description = "Decompiler's output language name. Current supported languages are: {java, cpp}.")
    private String myLanguage = "java";

    @Parameter(names = {"-h", "--help"}, description = "Print help information.", help = true)
    private boolean myHelp = false;

    @Parameter(names = {"-c", "--class"}, description = "Fully qualified class name (class is searched in current ClassLoader).")
    private String myClassName = null;

    @Parameter(names = {"-p", "--path"}, description = "Path to .class file.")
    private String myClassPath = null;

    @Parameter(names = {"-ns", "--tabsize"}, description = "Size of tabulation in class representation.")
    private int myTabSize = 4;

    @Parameter(names = {"-tw", "--textwidth"}, description = "Decompiled class code width.")
    private int myTextWidth = 100;

    public boolean isHelp() {
        return myHelp;
    }

    public String getLanguage() {
        return myLanguage;
    }

    public String getClassName() {
        return myClassName;
    }

    public String getClassPath() {
        return myClassPath;
    }

    public int getTextWidth() {
        return myTextWidth;
    }

    public int getTabSize() {
        return myTabSize;
    }
}
