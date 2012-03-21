/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.math.BigDecimal;
import java.util.ResourceBundle;

import org.beanfabrics.util.ResourceBundleFactory;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * The IntegerPM is a presentation model for integer numbers. The default range
 * for valid numbers is between {@link Long#MIN_VALUE} and
 * {@link Long#MAX_VALUE}.
 *
 * @author Michael Karneim
 */
public class IntegerPM extends BigDecimalPM implements IIntegerPM {
    protected static final String KEY_MESSAGE_VALUE_TOO_SMALL = "message.valueTooSmall";
    protected static final String KEY_MESSAGE_VALUE_TOO_BIG = "message.valueTooBig";
    protected static final String KEY_MESSAGE_INVALID_NUMBER = "message.invalidNumber";
    private final ResourceBundle resourceBundle = ResourceBundleFactory.getBundle(IntegerPM.class);
    private long minValue = Long.MIN_VALUE;
    private long maxValue = Long.MAX_VALUE;

    public IntegerPM() {
        // Please note: to disable default validation rules just call getValidator().clear();
        getValidator().add(new IntegerValidationRule());
    }

    public IntegerPM(Integer value) {
        this();
        setInteger(value);
    }

    public long getMinValue() {
        return minValue;
    }

    public void setMinValue(long minValue) {
        this.minValue = minValue;
        revalidate();
    }

    public long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
        revalidate();
    }

    public void setMinMaxValueRange(long minValue, long maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        revalidate();
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

    public class IntegerValidationRule implements ValidationRule {
        public ValidationState validate() {
            if (isEmpty()) {
                return null;
            }
            try {
                long value = getLong();
                if (value < minValue) {
                    String message = resourceBundle.getString(KEY_MESSAGE_VALUE_TOO_SMALL);
                    return new ValidationState(message);
                }
                if (value > maxValue) {
                    String message = resourceBundle.getString(KEY_MESSAGE_VALUE_TOO_BIG);
                    return new ValidationState(message);
                }
                return null;
            } catch (ConversionException e) {
                String message = resourceBundle.getString(KEY_MESSAGE_INVALID_NUMBER);
                return new ValidationState(message);
            }
        }
    }
}