/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import junit.framework.JUnit4TestAdapter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class BooleanPMTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BooleanPMTest.class);
    }

    public BooleanPMTest() {
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
    public void create() {
        new BooleanPM();
    }

    @Test
    public void setBoolean() {
        BooleanPM model = new BooleanPM();
        model.setBoolean(true);
        assertEquals("pM.getText()", model.getOptions().get(true), model.getText());
        model.setBoolean(false);
        assertEquals("pM.getText()", model.getOptions().get(false), model.getText());
        model.setBoolean(null);
        assertEquals("pM.getText()", "", model.getText());
    }

    @Test
    public void validate() {
        BooleanPM model = new BooleanPM();
        model.setText("hello");
        assertEquals("pM.isValid()", false, model.isValid());
        model.setText(model.getOptions().get(true));
        assertEquals("pM.isValid()", true, model.isValid());
        model.setText(null);
        assertEquals("pM.isValid()", false, model.isValid());
        model.setText(model.getOptions().get(false));
        assertEquals("pM.isValid()", true, model.isValid());
    }

    @Test
    public void getBoolean() {
        BooleanPM model = new BooleanPM();
        model.setText(model.getOptions().get(true));
        assertEquals("pM.getBoolean()", true, model.getBoolean());
        model.setText(model.getOptions().get(false));
        assertEquals("pM.getBoolean()", false, model.getBoolean());
        model.setText("");
        assertEquals("pM.getBoolean()", null, model.getBoolean());
    }

    @Test
    public void getInvalidBoolean() {
        BooleanPM model = new BooleanPM();
        model.setText("hello");
        try {
            model.getBoolean();
            fail("expected IllegalStateException");
        } catch (ConversionException ex) {
            // ok.
        }
    }

    @Test
    public void isModified() {
        BooleanPM pModel = new BooleanPM();
        pModel.setBoolean(true);
        pModel.preset();
        assertEquals("pModel.isModified()", false, pModel.isModified());
        pModel.setBoolean(false);
        assertEquals("pModel.isModified()", true, pModel.isModified());
        pModel.reset();
        assertEquals("pModel.isModified()", false, pModel.isModified());
    }

}