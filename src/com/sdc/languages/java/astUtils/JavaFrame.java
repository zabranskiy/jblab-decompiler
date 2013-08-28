package com.sdc.languages.java.astUtils;

import com.sdc.languages.general.astUtils.AbstractFrame;

public class JavaFrame extends AbstractFrame {
    @Override
    protected AbstractFrame createFrame() {
        return new JavaFrame();
    }
}
