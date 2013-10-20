package com.sdc.languages.java.astUtils;

import com.sdc.languages.general.astUtils.Frame;

import org.jetbrains.annotations.NotNull;


public class JavaFrame extends Frame {
    @NotNull
    @Override
    protected Frame createFrame() {
        return new JavaFrame();
    }
}
