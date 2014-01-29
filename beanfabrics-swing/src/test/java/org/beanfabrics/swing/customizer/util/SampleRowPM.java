package org.beanfabrics.swing.customizer.util;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;

public class SampleRowPM extends AbstractPM {
    TextPM colA = new TextPM();
    TextPM colB = new TextPM();
    
    public SampleRowPM() {
        PMManager.setup(this);
    }
}
