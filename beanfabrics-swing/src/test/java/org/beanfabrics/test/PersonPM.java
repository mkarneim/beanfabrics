/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
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
        public void execute() {
            result.setText("name=" + name + ", address=" + address.toString());
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