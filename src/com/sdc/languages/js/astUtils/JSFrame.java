package com.sdc.languages.js.astUtils;

import com.sdc.languages.general.astUtils.AbstractFrame;

public class JSFrame extends AbstractFrame {
    @Override
    protected AbstractFrame createFrame() {
        return new JSFrame();
    }
}
