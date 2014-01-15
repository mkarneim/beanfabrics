/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Currency;
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
    public static void setUpClass()
        throws Exception {
    }

    @AfterClass
    public static void tearDownClass()
        throws Exception {
    }

    MoneyPM create() {
        MoneyPM pm = new MoneyPM();
        pm.setFormat(new BigDecimalPM.Format(MoneyPM.getCurrencyFormat(Locale.GERMANY)));
        return pm;
    }

    @Test
    public void newInstance() {
        create();
    }


    @Test
    public void testCurrency() {
        Currency c = Currency.getInstance(Locale.JAPAN);
        String sym = c.getSymbol(Locale.US);
//        System.out.println(sym);
    }

    @Test
    public void validFormatsWithCurrencySymbol() {
        MoneyPM pm = create();
        pm.setText("14 \u20AC");
        assertEquals("pm.isValid()", true, pm.isValid());

        pm.setText("14,00 \u20AC");
        assertEquals("pm.isValid()", true, pm.isValid());

        pm.setText("140.000,00 \u20AC");
        assertEquals("pm.isValid()", true, pm.isValid());
    }

    @Test
    public void validFormatsWithoutCurrencySymbol() {
        MoneyPM pm = create();
        pm.setText("14");
        assertEquals("pm.isValid()", true, pm.isValid());

        pm.setText("14,00");
        assertEquals("pm.isValid()", true, pm.isValid());

        pm.setText("140.000,00");
        assertEquals("pm.isValid()", true, pm.isValid());
    }

    @Test
    public void invalidFormatsWithForeignCurrencySymbol() {
        MoneyPM pm = create();
        pm.setText("14 $");
        assertEquals("pm.isValid()", false, pm.isValid());
    }

    @Test
    public void invalidFormats() {
        MoneyPM pm = create();
        pm.setText("abc");
        assertEquals("pm.isValid()", false, pm.isValid());
    }

    //    @Test
    //    public void changeCurrency() {
    //        MoneyPM pm = create();
    //        pm.setCurrency( Currency.getInstance(Locale.US));
    //        pm.setText("100");
    //        assertEquals("pm.isValid()", true, pm.isValid());
    //        pm.reformat();
    //        String text = pm.getText();
    //        assertEquals("text", "100,00 $", text);
    //    }

    @Test
    public void setBigDecimal() {
        MoneyPM pm = create();
        BigDecimal val = new BigDecimal("1000.23");
        pm.setBigDecimal(val);

        BigDecimal res = pm.getBigDecimal();
        assertNotNull("res", res);
        assertEquals("res", val, res);
    }

    @Test
    public void getText() {
        MoneyPM pm = create();
        BigDecimal val = new BigDecimal("1000.23");
        pm.setBigDecimal(val);

        assertEquals("pm.getText()", "1.000,23 \u20AC", pm.getText());
    }
}