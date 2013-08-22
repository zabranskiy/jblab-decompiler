package com.sdc.js;

import com.sdc.abstractLanguage.AbstractFrame;

public class JSFrame extends AbstractFrame {
    @Override
    protected AbstractFrame createFrame() {
        return new JSFrame();
    }
}
