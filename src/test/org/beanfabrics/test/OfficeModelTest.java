/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.Path;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.support.PropertySupport;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class OfficeModelTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(OfficeModelTest.class);
    }

    OfficePM model;

    public OfficeModelTest() {
    }

    @Before
    public void setUp()
        throws Exception {
        model = new OfficePM();
        model.name.setText("Software Development");
        model.address.street.setText("12th Main Street");
        model.address.appartment.setText("App. No. 14/3");
        model.address.city.setText("Boston");
        model.address.country.setText("USA");
        model.address.zip.setText("02106");
    }

    @AfterClass
    public static void tearDownClass()
        throws Exception {
    }

    @Test
    public void getText()
        throws Throwable {
        assertEquals("model.address.zip.getText()", "02106", model.address.zip.getText());
        ITextPM zipProp = (ITextPM)PropertySupport.get(model).getProperty(new Path("this.address.zip"));
        assertEquals("model.address.zip", zipProp, model.address.zip);
    }
}