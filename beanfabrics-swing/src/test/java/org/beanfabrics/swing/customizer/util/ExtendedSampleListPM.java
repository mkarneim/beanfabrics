package org.beanfabrics.swing.customizer.util;

import org.beanfabrics.model.PMManager;

public class ExtendedSampleListPM<PM extends ExtendedSampleRowPM> extends SampleListPM<PM> {
    public ExtendedSampleListPM() {
        PMManager.setup(this);
    }
}
