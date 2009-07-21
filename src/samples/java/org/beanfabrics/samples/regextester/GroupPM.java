package org.beanfabrics.samples.regextester;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;

/**
 * The GroupPM is the presentation model for showing a text that matches a group
 * inside a regular expression.
 */
public class GroupPM extends AbstractPM {
    IntegerPM findIndex = new IntegerPM();
    IntegerPM groupIndex = new IntegerPM();
    TextPM text = new TextPM();

    public GroupPM(int findIndex, int groupIndex, String groupText) {
        this.findIndex.setInteger(findIndex);
        this.findIndex.setEditable(false);
        this.groupIndex.setInteger(groupIndex);
        this.groupIndex.setEditable(false);
        this.text.setText(groupText);
        this.text.setEditable(false);
        PMManager.setup(this);
    }
}
