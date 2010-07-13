/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import junit.framework.JUnit4TestAdapter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class AddressModelTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AddressModelTest.class);
    }

    public AddressModelTest() {
    }

    @BeforeClass
    public static void setUpClass()
        throws Exception {
    }

    @AfterClass
    public static void tearDownClass()
        throws Exception {
    }

    @Test
    public void create()
        throws Throwable {
        AddressPM pModel = new AddressPM();
        pModel.street.setText("12th Main Street");
        pModel.appartment.setText("App. No. 14/3");
        pModel.city.setText("Boston");
        pModel.zip.setText("02105");
        pModel.country.setText("USA");
    }

    @Test
    public void validate()
        throws Throwable {
        AddressPM pModel = new AddressPM();
        pModel.street.setText("12th Main Street");
        pModel.appartment.setText("App. No. 14/3");
        pModel.city.setText("Boston");
        //pModel.zip.setText("02105");
        pModel.country.setText("USA");

        assertEquals("pModel.zip.isValid()", false, pModel.zip.isValid()); // zip is mandatory
        assertNotNull("pModel.zip.getValidationState()", pModel.zip.getValidationState());
        //		assertEquals("pModel.isValid()", false, pModel.isValid()); // since zip is mandatory and AddressPM has a validator that depends on it

        pModel.zip.setText("02105");
        assertEquals("pModel.zip.isValid()", true, pModel.zip.isValid()); // zip is mandatory
        assertNull("pModel.zip.getValidationState()", pModel.zip.getValidationState());
        //		assertEquals("pModel.isValid()", true, pModel.isValid()); // zip is mandatory
    }
}