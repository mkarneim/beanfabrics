package org.beanfabrics.samples.properties;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;

/**
 * The EntryPM is a presentation model that holds a key-value-pair.
 */
public class EntryPM extends AbstractPM {
    public final TextPM key = new TextPM();
    public final TextPM value = new TextPM();

    public EntryPM() {
        key.setEditable(false);
        value.setEditable(false);
        PMManager.setup(this);
    }

    public EntryPM(String aKey, String aValue) {
        this();
        key.setText(aKey);
        value.setText(aValue);
    }
}
