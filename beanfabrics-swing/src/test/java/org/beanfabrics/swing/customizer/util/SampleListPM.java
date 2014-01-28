package org.beanfabrics.swing.customizer.util;

import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.PMManager;

public class SampleListPM<PM extends SampleRowPM> extends ListPM<PM> {
    public SampleListPM() {
        PMManager.setup(this);
    }
}
