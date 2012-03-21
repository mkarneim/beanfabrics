/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class CalculatorModelTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CalculatorModelTest.class);
    }

    public CalculatorModelTest() {
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
        new CalculatorPM();
    }

    @Test
    public void simplePlus()
        throws Throwable {
        CalculatorPM pModel = new CalculatorPM();
        pModel.input.setText("12");
        pModel.plus.execute();
        pModel.input.setText("13");
        pModel.showResult.execute();
        assertEquals("pModel.input.getInteger()", 25, (int)pModel.input.getInteger());
    }

    @Test
    public void simpleMinus()
        throws Throwable {
        CalculatorPM pModel = new CalculatorPM();
        pModel.input.setText("12");
        pModel.minus.execute();
        pModel.input.setText("5");
        pModel.showResult.execute();
        assertEquals("pModel.input.getInteger()", 7, (int)pModel.input.getInteger());
    }

    @Test
    public void plusplus()
        throws Throwable {
        CalculatorPM pModel = new CalculatorPM();
        pModel.input.setText("12");
        pModel.plus.execute();
        assertEquals("pModel.input.getInteger()", 12, (int)pModel.input.getInteger());
        pModel.input.setText("13");
        pModel.plus.execute();
        assertEquals("pModel.input.getInteger()", 25, (int)pModel.input.getInteger());
        pModel.input.setText("14");
        pModel.showResult.execute();
        assertEquals("pModel.input.getInteger()", 39, (int)pModel.input.getInteger());
    }

    @Test
    public void plusminus()
        throws Throwable {
        CalculatorPM pModel = new CalculatorPM();
        pModel.input.setText("12");
        pModel.plus.execute();
        assertEquals("pModel.input.getInteger()", 12, (int)pModel.input.getInteger());
        pModel.input.setText("13");
        pModel.minus.execute();
        assertEquals("pModel.input.getInteger()", 25, (int)pModel.input.getInteger());
        pModel.input.setText("14");
        pModel.showResult.execute();
        assertEquals("pModel.input.getInteger()", 11, (int)pModel.input.getInteger());
    }

    @Test
    public void minusplus()
        throws Throwable {
        CalculatorPM pModel = new CalculatorPM();
        pModel.input.setText("12");
        pModel.minus.execute();
        assertEquals("pModel.input.getInteger()", 12, (int)pModel.input.getInteger());
        pModel.input.setText("13");
        pModel.plus.execute();
        assertEquals("pModel.input.getInteger()", -1, (int)pModel.input.getInteger());
        pModel.input.setText("14");
        pModel.showResult.execute();
        assertEquals("pModel.input.getInteger()", 13, (int)pModel.input.getInteger());
    }
}