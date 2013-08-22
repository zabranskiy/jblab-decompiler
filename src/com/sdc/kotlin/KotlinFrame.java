package com.sdc.kotlin;

import com.sdc.abstractLanguage.AbstractFrame;

public class KotlinFrame extends AbstractFrame {
    @Override
    protected AbstractFrame createFrame() {
        return new KotlinFrame();
    }
}
