/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Property;

public class ContactFilterPM extends AbstractPM {
    @Property
    private TextPM searchString = new TextPM();
    @Property
    private IOperationPM filter = new OperationPM();
    private Target target;

    public ContactFilterPM() {
        PMManager.setup(this);
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    @Operation
    public void filter() {
        filter.check();
        this.target.filter(searchString.getText());
    }

    public interface Target {
        public void filter(String text);
    }
}