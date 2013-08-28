package com.sdc.languages.java.astUtils;

import com.sdc.languages.general.astUtils.Frame;

public class JavaFrame extends Frame {
    @Override
    protected Frame createFrame() {
        return new JavaFrame();
    }
}
