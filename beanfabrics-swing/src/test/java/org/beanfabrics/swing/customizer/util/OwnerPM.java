package org.beanfabrics.swing.customizer.util;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;

public class OwnerPM extends AbstractPM {
    SampleListPM<SampleRowPM> list = new SampleListPM<SampleRowPM>();

    public OwnerPM() {
        PMManager.setup(this);
    }
}
