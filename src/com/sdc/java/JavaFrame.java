package com.sdc.java;

import com.sdc.abstractLanguage.AbstractFrame;

public class JavaFrame extends AbstractFrame {
    @Override
    protected AbstractFrame createFrame() {
        return new JavaFrame();
    }
}
