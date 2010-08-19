package org.beanfabrics.samples.properties;

import java.util.Properties;

import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.OnChange;

/**
 * The PropertiesPM is a presentation model that shows the contents of a
 * {@link Properties} object.
 */
public class PropertiesPM extends ListPM<EntryPM> {
    public final IntegerPM numberOfSelectedEntries = new IntegerPM();
    public final IntegerPM numberOfEntries = new IntegerPM();

    public final TextPM status = new TextPM();

    public PropertiesPM() {
        PMManager.setup(this);
    }

    public PropertiesPM(Properties props) {
        this();
        setProperties(props);
    }

    public void setProperties(Properties props) {
        clear();
        for (Object keyObj : props.keySet()) {
            String key = (String)keyObj;
            String value = props.getProperty(key);
            add(new EntryPM(key, value));
        }
    }

    @OnChange
    void updateNumberOfSelectedEntries() {
        numberOfSelectedEntries.setInteger(getSelection().size());
    }

    @OnChange
    void updateNumberOfEntries() {
        numberOfEntries.setInteger(size());
    }

    @OnChange(path = { "numberOfSelectedEntries", "numberOfEntries" })
    void updateStatus() {
        String newText = "";
        if (numberOfEntries.getInteger() == 1) {
            newText = "1 entry";
        } else {
            newText = numberOfEntries.getInteger() + " entries";
        }
        if (numberOfSelectedEntries.getInteger() == 1) {
            newText = newText + " / 1 selected";
        } else if (numberOfSelectedEntries.getInteger() > 1) {
            newText = newText + " / " + numberOfSelectedEntries.getInteger() + " selected";
        }
        status.setText(newText);
    }
}
