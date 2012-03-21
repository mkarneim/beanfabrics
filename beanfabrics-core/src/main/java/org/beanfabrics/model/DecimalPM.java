/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import java.math.BigDecimal;

/**
 * The {@link DecimalPM} is a {@link PresentationModel} that contains a decimal
 * number. It offers setter and getter methods for Double and Float.
 * 
 * @author Michael Karneim
 */
public class DecimalPM extends BigDecimalPM {
    /**
     * Constructs a {@link DecimalPM}.
     */
    public DecimalPM() {

    }

    /**
     * Returns the value of this PM as a {@link Double}.
     * 
     * @return the value of this PM as a Double
     * @throws ConversionException if the text value of this PM can't be
     *             converted to a double value using the defined format
     * @see #setFormat(java.text.DecimalFormat)
     * @see #getText()
     */
    public Double getDouble()
        throws ConversionException {
        if (this.isEmpty()) {
            return null;
        } else {
            BigDecimal bd = this.getBigDecimal();
            return bd.doubleValue();
        }
    }

    /**
     * Sets the value of this PM to the given {@link Double} value.
     * 
     * @param value the double value
     * @see #setText(String)
     */
    public void setDouble(Double value) {
        if (value == null) {
            this.setBigDecimal(null);
        } else {
            this.setBigDecimal(new BigDecimal(value));
        }
    }

    /**
     * Returns the value of this PM as a {@link Float}.
     * 
     * @return the value of this PM as a Float
     * @throws ConversionException if the text value of this PM can't be
     *             converted to a float value using the defined format
     */
    public Float getFloat()
        throws ConversionException {
        if (this.isEmpty()) {
            return null;
        } else {
            BigDecimal bd = this.getBigDecimal();
            return bd.floatValue();
        }
    }

    /**
     * Sets the value of this PM to the given {@link Float}.
     * 
     * @param value the float value
     */
    public void setFloat(Float value) {
        if (value == null) {
            this.setBigDecimal(null);
        } else {
            this.setBigDecimal(new BigDecimal(value));
        }
    }

    public void setByte(Byte value) {
        if (value == null) {
            setBigDecimal(null);
        } else {
            setBigDecimal(new BigDecimal(value));
        }
    }

    public Byte getByte()
        throws ConversionException {
        if (isEmpty()) {
            return null;
        } else {
            try {
                return getBigDecimal().byteValueExact();
            } catch (ArithmeticException ex) {
                throw new ConversionException(ex);
            }
        }
    }

    public void setShort(Short value) {
        if (value == null) {
            setBigDecimal(null);
        } else {
            setBigDecimal(new BigDecimal(value));
        }
    }

    public Short getShort()
        throws ConversionException {
        if (isEmpty()) {
            return null;
        } else {
            try {
                return getBigDecimal().shortValueExact();
            } catch (ArithmeticException ex) {
                throw new ConversionException(ex);
            }
        }
    }

    public void setInteger(Integer value) {
        if (value == null) {
            setBigDecimal(null);
        } else {
            setBigDecimal(new BigDecimal(value));
        }
    }

    public Integer getInteger()
        throws ConversionException {
        if (isEmpty()) {
            return null;
        } else {
            try {
                return getBigDecimal().intValueExact();
            } catch (ArithmeticException ex) {
                throw new ConversionException(ex);
            }
        }
    }

    public void setLong(Long value) {
        if (value == null) {
            setBigDecimal(null);
        } else {
            setBigDecimal(new BigDecimal(value));
        }
    }

    public Long getLong()
        throws ConversionException {
        if (isEmpty()) {
            return null;
        } else {
            try {
                return getBigDecimal().longValueExact();
            } catch (ArithmeticException ex) {
                throw new ConversionException(ex);
            }
        }
    }
}