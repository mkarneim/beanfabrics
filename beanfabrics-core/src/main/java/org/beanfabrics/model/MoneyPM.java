/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * The MoneyPM is a presentation model for a monetary value with a currency
 * format.
 * 
 * @author Michael Karneim
 */
// TODO (mk) move this class to beanfabrics-ext.jar
public class MoneyPM extends BigDecimalPM {

    public MoneyPM() {
    }

    @Override
    protected IFormat<BigDecimal> createDefaultFormat() {
        return new Format(getCurrencyFormat(Locale.getDefault()));
    }

    protected static DecimalFormat getCurrencyFormat(Locale locale) {
        DecimalFormat result = (DecimalFormat)NumberFormat.getCurrencyInstance(locale);
        result.setParseBigDecimal(true);
        return result;
    }
}