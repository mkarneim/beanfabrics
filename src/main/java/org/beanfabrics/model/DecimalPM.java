/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.math.BigDecimal;

/**
 * The DecimalPM is a presentation model for decimal numbers. It offers
 * conversion methods for Double and Float.
 * 
 * @author Michael Karneim
 */
// TODO UNIT TEST
public class DecimalPM extends BigDecimalPM {
    public DecimalPM() {

    }

    public Double getDouble()
        throws ConversionException {
        if (this.isEmpty()) {
            return null;
        } else {
            BigDecimal bd = this.getBigDecimal();
            return bd.doubleValue();
        }
    }

    public void setDouble(Double value) {
        if (value == null) {
            this.setBigDecimal(null);
        } else {
            this.setBigDecimal(new BigDecimal(value));
        }
    }

    public Float getFloat()
        throws ConversionException {
        if (this.isEmpty()) {
            return null;
        } else {
            BigDecimal bd = this.getBigDecimal();
            return bd.floatValue();
        }
    }

    public void setFloat(Float value) {
        if (value == null) {
            this.setBigDecimal(null);
        } else {
            this.setBigDecimal(new BigDecimal(value));
        }
    }
}