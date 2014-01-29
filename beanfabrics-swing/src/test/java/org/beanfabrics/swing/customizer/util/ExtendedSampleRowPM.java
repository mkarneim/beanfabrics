package org.beanfabrics.swing.customizer.util;

import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;

public class ExtendedSampleRowPM extends SampleRowPM {
    TextPM colC = new TextPM();

    public ExtendedSampleRowPM() {
        PMManager.setup(this);
    }
}
