/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class BigDecimalPMTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BigDecimalPMTest.class);
    }

    static Locale oldLocale;

    @BeforeClass
    public static void setupClass() {
        oldLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    public static void tearDownClass() {
        Locale.setDefault(oldLocale);
    }

    @Test
    public void create() {
        new BigDecimalPM();
    }

    @Test
    public void setText1() {
        Locale.setDefault(Locale.ENGLISH);
        BigDecimalPM pm = new BigDecimalPM();
        pm.setText("1234.56");
        BigDecimal bd = new BigDecimal("1234.56");
        assertEquals("pm.getBigDecimal()", bd, pm.getBigDecimal());
    }

    @Test
    public void setText2() {
        BigDecimalPM pm = new BigDecimalPM();
        pm.setText("1234abc");
        assertEquals("pm.isValid()", false, pm.isValid());
    }

    @Test
    public void setText3() {
        BigDecimalPM pm = new BigDecimalPM();
        pm.setText("9223372036854775809");
        assertEquals("pm.isValid()", true, pm.isValid());
    }

    @Test
    public void validationMessageIsLocalized() {
        Locale old = Locale.getDefault();
        try {
            Locale.setDefault(Locale.GERMAN);
            BigDecimalPM pm = new BigDecimalPM();
            pm.setText("blah"); // invalid text
            assertFalse("pm.isValid()", pm.isValid());
            String message = pm.getValidationState().getMessage();
            assertEquals("message", "Dies ist keine korrekte Zahl", message);
        } finally {
            Locale.setDefault(old);
        }
    }

    @Test
    public void formatting() {
        Locale.setDefault(Locale.ENGLISH);
        BigDecimalPM pm = new BigDecimalPM();
        DecimalFormat format = new DecimalFormat("#.##");
        pm.setFormat(new BigDecimalPM.Format(format));
        pm.setText("125.5678");
        assertEquals("pm.getText()", "125.5678", pm.getText());

        pm.reformat();

        assertEquals("pm.isValid()", true, pm.isValid());
        assertEquals("pm.getText()", "125.57", pm.getText());
    }

    @Test
    public void setFormat() {
        Locale.setDefault(Locale.GERMANY);
        BigDecimalPM pm = new BigDecimalPM();
        DecimalFormat format = (DecimalFormat)NumberFormat.getNumberInstance(Locale.GERMANY);
        pm.setFormat(new BigDecimalPM.Format(format));
        pm.setText("1.234.567,89");

        DecimalFormat f2 = (DecimalFormat)NumberFormat.getNumberInstance(Locale.US);
        pm.setFormat(new BigDecimalPM.Format(f2));

        assertEquals("1,234,567.89", pm.getText());
    }

    @Test
    public void setFormatWithInvalidNumber() {
        Locale.setDefault(Locale.GERMANY);
        BigDecimalPM pm = new BigDecimalPM();
        DecimalFormat format = (DecimalFormat)NumberFormat.getNumberInstance(Locale.GERMANY);
        pm.setFormat(new BigDecimalPM.Format(format));
        pm.setText("abcxyz");

        DecimalFormat f2 = (DecimalFormat)NumberFormat.getNumberInstance(Locale.US);
        pm.setFormat(new BigDecimalPM.Format(f2));

        assertEquals("abcxyz", pm.getText());
    }
}