/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Locale;

import junit.framework.JUnit4TestAdapter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MoneyPMTest {
	public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(MoneyPMTest.class);
    }

    public MoneyPMTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    MoneyPM create() {
    	MoneyPM cell = new MoneyPM();
    	cell.setFormat( MoneyPM.getCurrencyFormat(Locale.GERMANY));
    	return cell;
    }

    @Test
    public void newInstance() {
    	create();
    }

    @Test
    public void validate() {
    	MoneyPM cell = create();
    	cell.setText("14 \u20AC");
    	assertEquals("pM.isValid()",true, cell.isValid());

    	cell.setText("14,00 \u20AC");
    	assertEquals("pM.isValid()",true, cell.isValid());

    	cell.setText("140.000,00 \u20AC");
    	assertEquals("pM.isValid()",true, cell.isValid());


    	cell.setText("abc");
    	assertEquals("pM.isValid()",false, cell.isValid());
    }

    @Test
    public void setBigDecimal() {
    	MoneyPM cell = create();
    	BigDecimal val = new BigDecimal("1000.23");
    	cell.setBigDecimal(val);

    	BigDecimal res = cell.getBigDecimal();
    	assertNotNull("res",res);
    	assertEquals( "res", val, res);
    }

    @Test
    public void getText() {
    	MoneyPM cell = create();
    	BigDecimal val = new BigDecimal("1000.23");
    	cell.setBigDecimal(val);

    	assertEquals("pM.getText()", "1.000,23 \u20AC", cell.getText());
    }
}