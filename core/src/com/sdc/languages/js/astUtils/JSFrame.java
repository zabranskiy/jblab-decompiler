package com.sdc.languages.js.astUtils;

import com.sdc.languages.general.astUtils.Frame;

public class JSFrame extends Frame {
    @Override
    protected Frame createFrame() {
        return new JSFrame();
    }
}
