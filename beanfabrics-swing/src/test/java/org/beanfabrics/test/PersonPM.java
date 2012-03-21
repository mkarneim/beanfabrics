/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import org.beanfabrics.model.AbstractOperationPM;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Property;

/**
 * @author Michael Karneim
 */
public class PersonPM extends AbstractPM {
    @Property
    public final TextPM name = new TextPM();
    @Property
    public AddressPM address;
    @Property
    public final IOperationPM save = new AbstractOperationPM() {
        public boolean execute() {
            result.setText("name=" + name + ", address=" + address.toString());
            return true; // success
        }
    };
    @Property
    public final TextPM result = new TextPM();

    public PersonPM() {
        init();
    }

    private void init() {
        address = new AddressPM();
        PMManager.setup(this);
    }
}