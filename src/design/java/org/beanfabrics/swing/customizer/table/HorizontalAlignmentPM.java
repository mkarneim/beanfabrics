package org.beanfabrics.swing.customizer.table;

import javax.swing.SwingConstants;

import org.beanfabrics.model.Options;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;

public class HorizontalAlignmentPM extends TextPM {
    private static final Options<Integer> opts = createOptions();

    public HorizontalAlignmentPM() {
        setOptions(opts);
        PMManager.setup(this);
    }

    private static Options<Integer> createOptions() {
        Options<Integer> result = new Options<Integer>();
        result.put(null, "");
        result.put(SwingConstants.LEADING, "Leading");
        result.put(SwingConstants.LEFT, "Left");
        result.put(SwingConstants.CENTER, "Center");
        result.put(SwingConstants.TRAILING, "Trailing");
        result.put(SwingConstants.RIGHT, "Right");
        return result;
    }
}
