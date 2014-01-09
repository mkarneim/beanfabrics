/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.DatePM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.IconPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Validation;

/**
 * @author Michael Karneim
 */
public class ContactPM extends AbstractPM {
    final TextPM firstname = new TextPM();
    final TextPM lastname = new TextPM();
    final DatePM birthday = new DatePM();
    final IntegerPM children = new IntegerPM();
    final BooleanPM isMarried = new BooleanPM();
    final IconPM icon = new IconPM();
    final NotePM notes = new NotePM();
    //	final TextPM row1 = new TextPM();
    //	final TextPM row2 = new TextPM();
    //	final TextPM row3 = new TextPM();
    //	final TextPM row4 = new TextPM();
    //	final TextPM row5 = new TextPM();
    //	final TextPM row6 = new TextPM();
    //	final TextPM row7 = new TextPM();
    //	final TextPM row8 = new TextPM();
    //	final TextPM row9 = new TextPM();
    final IOperationPM addChild = new OperationPM();
    final AddressPM address = new AddressPM();

    public ContactPM() {
        PMManager.setup(this);
        icon.setIconUrl(ContactPM.class.getResource("/org/beanfabrics/swing/beanfabrics16x16.png"));
    }

    @Validation(path = "addChild", message = "your are single")
    public boolean validateAddChildren() {
        return isMarried.isValid() && !isMarried.isEmpty() && isMarried.getBoolean();
    }

    @Operation
    public void addChild() {
        int num = children.getInteger() == null ? 0 : children.getInteger();
        children.setInteger(num + 1);
    }

    public String paramString() {
        return "" + firstname + " " + lastname + " " + birthday + " " + address.toString();
    }
}