/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Property;

public class ProductPM extends AbstractPM {
    @Property
    TextPM name = new TextPM();
    @Property
    TextPM type = new TextPM();
    @Property
    IntegerPM price = new IntegerPM();
    @Property
    TextPM country = new TextPM();

    public ProductPM() {
        PMManager.setup(this);
    }
}