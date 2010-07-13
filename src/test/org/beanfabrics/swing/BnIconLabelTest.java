/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.model.IconPM;
import org.beanfabrics.model.IconTextPM;
import org.junit.Test;

/**
 * @author Michael Karneim
 * @author Marcel Eyke
 */
public class BnIconLabelTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BnIconLabelTest.class);
    }

    public BnIconLabelTest() {
    }

    @Test
    public void setPresentationModel() {
        BnIconLabel lbl = new BnIconLabel();

        // connect with icon model an text must be blank
        IconPM icon = new IconPM();
        icon.setIconUrl(BnLabelTest.class.getResource("sample.gif"));
        lbl.setPresentationModel(icon);
        assertEquals("lbl.isConnected()", true, lbl.isConnected());
        assertEquals("lbl.getText()", "", lbl.getText());
        assertNotNull("lbl.getIcon()", lbl.getIcon());

        // connect with icon and text model
        IconTextPM iconText = new IconTextPM();
        iconText.setIconUrl(BnLabelTest.class.getResource("sample.gif"));
        iconText.setText("hello, world!");
        lbl.setPresentationModel(iconText);
        assertEquals("lbl.isConnected()", true, lbl.isConnected());
        assertEquals("lbl.getText()", "hello, world!", lbl.getText());
        assertNotNull("lbl.getIcon()", lbl.getIcon());

        // disconnect
        lbl.setPresentationModel(null);
        assertEquals("lbl.isConnected()", false, lbl.isConnected());
        assertEquals("lbl.getText()", "", lbl.getText());

    }
}