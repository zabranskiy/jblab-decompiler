package com.decompiler;

import com.beust.jcommander.Parameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class DecompilerParameters {
    @Parameter(description = "Prints a disassembled view of the given class using the specified language.")
    private List<String> myParameters = new ArrayList<String>();

    @Parameter(names = {"-l", "--language"},
               description = "Decompiler's output language name. Currently supported languages are: {java, javascript, kotlin}.")
    private String myLanguage = "trace";

    @Parameter(names = {"-h", "--help"}, description = "Prints help information.", help = true)
    private boolean myHelp = false;

    @Parameter(names = {"-c", "--class"},
               description = "Fully qualified class name (class will be searched in the current ClassLoader).")
    private String myClassName = null;

    @Parameter(names = {"-p", "--path"}, description = "Path to .class file.")
    private String myClassPath = null;

    @Parameter(names = {"-ns", "--tabsize"}, description = "Size of tabulation in class representation.")
    private int myTabSize = 4;

    @Parameter(names = {"-tw", "--textwidth"}, description = "Decompiled class code width.")
    private int myTextWidth = 100;

    @Parameter(names = {"-g", "--graph"}, description = "Draws decompiled class method graphs.")
    private boolean myGraphDrawer = false;

    public boolean isHelp() {
        return myHelp;
    }

    @NotNull
    public String getLanguage() {
        return myLanguage;
    }

    @Nullable
    public String getClassName() {
        return myClassName;
    }

    @Nullable
    public String getClassPath() {
        return myClassPath;
    }

    public int getTextWidth() {
        return myTextWidth;
    }

    public int getTabSize() {
        return myTabSize;
    }

    public boolean isGraphDrawer() {
        return myGraphDrawer;
    }
}
