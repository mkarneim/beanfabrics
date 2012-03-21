/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class BigDecimalPMTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BigDecimalPMTest.class);
    }

    static Locale oldLocale;

    @Before
    public void setTemporaryDefaultLocale() {
        oldLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    @After
    public void resetTemporaryDefaultLocale() {
        Locale.setDefault(oldLocale);
    }

    @Test
    public void create() {
        new BigDecimalPM();
    }

    @Test
    public void canConvertTextIntoBigDecimal() {
        // Given:
        BigDecimalPM pm = new BigDecimalPM();
        // When:
        pm.setText("1234.56");
        // Then:
        BigDecimal bd = new BigDecimal("1234.56");
        assertEquals("pm.getBigDecimal()", bd, pm.getBigDecimal());
    }

    @Test
    public void alphanumericTextMustBeInvalid() {
        // Given:
        BigDecimalPM pm = new BigDecimalPM();
        // When:
        pm.setText("1234abc");
        // Then:
        assertEquals("pm.isValid()", false, pm.isValid());
    }

    @Test
    public void reallyBigIntegerIsValid() {
        // Given:
        BigDecimalPM pm = new BigDecimalPM();
        // When:
        pm.setText("9223372036854775809");
        // Then:
        assertEquals("pm.isValid()", true, pm.isValid());
    }

    @Test
    public void validationMessageIsLocalized() {
        // Given:
        Locale.setDefault(Locale.GERMAN);
        BigDecimalPM pm = new BigDecimalPM();
        // When:
        pm.setText("blah"); // invalid text
        // Then:
        assertFalse("pm.isValid()", pm.isValid());
        String message = pm.getValidationState().getMessage();
        assertEquals("message", "Dies ist keine korrekte Zahl", message);
    }

    @Test
    public void canReformatTextUsingCustomFormat() {
        // Given:
        BigDecimalPM pm = new BigDecimalPM();
        DecimalFormat format = new DecimalFormat("#.##");
        pm.setFormat(new BigDecimalPM.Format(format));
        // When:        
        pm.setText("125.5678");
        assertEquals("pm.getText()", "125.5678", pm.getText());
        pm.reformat();
        // Then:
        assertEquals("pm.isValid()", true, pm.isValid());
        assertEquals("pm.getText()", "125.57", pm.getText());
    }

    @Test
    public void settingAFormatDoesReformatText() {
        // Given:
        Locale.setDefault(Locale.GERMANY);
        BigDecimalPM pm = new BigDecimalPM();
        DecimalFormat germanFormat = (DecimalFormat)NumberFormat.getNumberInstance(Locale.GERMANY);
        DecimalFormat usFormat = (DecimalFormat)NumberFormat.getNumberInstance(Locale.US);
        pm.setFormat(new BigDecimalPM.Format(germanFormat));
        // When:
        pm.setText("1.234.567,89");
        pm.setFormat(new BigDecimalPM.Format(usFormat));
        // Then:
        assertEquals("1,234,567.89", pm.getText());
    }

    @Test
    public void settingAFormatOnInvalidContentDoesNotReformatText() {
        // Given:
        Locale.setDefault(Locale.GERMANY);
        BigDecimalPM pm = new BigDecimalPM();
        DecimalFormat germanFormat = (DecimalFormat)NumberFormat.getNumberInstance(Locale.GERMANY);
        DecimalFormat usFormat = (DecimalFormat)NumberFormat.getNumberInstance(Locale.US);
        pm.setFormat(new BigDecimalPM.Format(germanFormat));
        // When:
        pm.setText("abcxyz");
        pm.setFormat(new BigDecimalPM.Format(usFormat));
        // Then:
        assertEquals("abcxyz", pm.getText());
    }

    @Test
    public void canUsePercentSignInFormat() {
        // Given:
        BigDecimalPM pm = new BigDecimalPM();
        pm.setFormat(new BigDecimalPM.Format(new DecimalFormat("#.##%")));
        // When:
        pm.setBigDecimal(new BigDecimal("0.5123"));
        // Then:
        assertEquals("51.23%", pm.getText());
    }

    @Test
    public void textWithoutPercentSignIsValid() {
        // Given:
        BigDecimalPM pm = new BigDecimalPM();
        pm.setFormat(new BigDecimalPM.Format(new DecimalFormat("#.##%")));
        String input = "10";

        // When:
        pm.setText(input);

        // Then:        
        assertEquals("pm.isValid()", true, pm.isValid());
    }

    @Test
    public void reformattingANumberWithoutPercentageSignDoesMultiplyItWithMultiplier() {
        // Given:
        BigDecimalPM pm = new BigDecimalPM();
        DecimalFormat percentFormat = new DecimalFormat("#.##%");
        //percentFormat.setMultiplier(100); <- This is default
        pm.setFormat(new BigDecimalPM.Format(percentFormat));
        // When:
        pm.setText("0.5123");
        // Then:
        assertEquals("pm.isValid()", true, pm.isValid());
        pm.reformat();
        assertEquals("51.23%", pm.getText());
    }

    @Test
    public void reformattingANumberWithPercentageSignDoesNotMultiplyItWithMultiplier() {
        // Given:
        BigDecimalPM pm = new BigDecimalPM();
        DecimalFormat percentFormat = new DecimalFormat("#.##%");
        //percentFormat.setMultiplier(100); <- This is default
        pm.setFormat(new BigDecimalPM.Format(percentFormat));
        // When:
        pm.setText("51.23%");
        // Then:
        assertEquals("pm.isValid()", true, pm.isValid());
        pm.reformat();
        assertEquals("51.23%", pm.getText());
    }

}