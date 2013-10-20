package com.sdc.languages.js.astUtils;

import com.sdc.languages.general.astUtils.Frame;

import org.jetbrains.annotations.NotNull;


public class JSFrame extends Frame {
    @NotNull
    @Override
    protected Frame createFrame() {
        return new JSFrame();
    }
}
