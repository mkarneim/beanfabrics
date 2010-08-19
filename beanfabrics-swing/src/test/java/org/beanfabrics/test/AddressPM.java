/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;

/**
 * @author Michael Karneim
 */
public class AddressPM extends AbstractPM {
    public final TextPM appartment = new TextPM();
    public final TextPM street = new TextPM();
    public final TextPM city = new TextPM();
    public final TextPM zip = new TextPM() {
        // test subclass
    };
    public final TextPM country = new TextPM();

    public AddressPM() {
        PMManager.setup(this);
        init();
    }

    private void init() {
        appartment.setTitle("Appartment");
        street.setTitle("Street");
        city.setTitle("City");
        zip.setTitle("Zip");
        country.setTitle("Country");
        zip.setMandatory(true);
    }

    public String toString() {
        return "" + appartment + " " + street + " " + city + " " + zip + " " + country;
    }
}