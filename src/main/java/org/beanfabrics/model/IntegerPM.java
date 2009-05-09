/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.model;

import java.math.BigDecimal;

import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * The IntegerPM is a presentation model for integer numbers. The default range for valid numbers
 * is between {@link Long#MIN_VALUE} and {@link Long#MAX_VALUE}.
 *
 * @author Michael Karneim
 */
public class IntegerPM extends BigDecimalPM implements IIntegerPM {
	private long minValue = Long.MIN_VALUE;
	private long maxValue = Long.MAX_VALUE;

	public IntegerPM() {
		this.getValidator().add( new DefaultValidationRule());
	}

	public long getMinValue() {
		return minValue;
	}

	public void setMinValue(long minValue) {
		this.minValue = minValue;
		this.revalidate();
	}

	public long getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(long maxValue) {
		this.maxValue = maxValue;
		this.revalidate();
	}

	public void setMinMaxValueRange(long minValue, long maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.revalidate();
	}

	public void setByte( Byte value) {
		if ( value == null) {
			setBigDecimal(null);
		} else {
			setBigDecimal( new BigDecimal(value));
		}
	}

	public Byte getByte() throws ConversionException {
		if ( isEmpty()) {
			return null;
		} else {
			try {
				return getBigDecimal().byteValueExact();
			} catch (ArithmeticException ex) {
				throw new ConversionException(ex);
			}
		}
	}

	public void setShort( Short value) {
		if ( value == null) {
			setBigDecimal(null);
		} else {
			setBigDecimal( new BigDecimal(value));
		}
	}

	public Short getShort() throws ConversionException {
		if ( isEmpty()) {
			return null;
		} else {
			try {
				return getBigDecimal().shortValueExact();
			} catch (ArithmeticException ex) {
				throw new ConversionException(ex);
			}
		}
	}

	public void setInteger( Integer value) {
		if ( value == null) {
			setBigDecimal(null);
		} else {
			setBigDecimal( new BigDecimal(value));
		}
	}

	public Integer getInteger() throws ConversionException {
		if ( isEmpty()) {
			return null;
		} else {
			try {
				return getBigDecimal().intValueExact();
			} catch (ArithmeticException ex) {
				throw new ConversionException(ex);
			}
		}
	}

	public void setLong( Long value) {
		if ( value == null) {
			setBigDecimal(null);
		} else {
			setBigDecimal( new BigDecimal(value));
		}
	}

	public Long getLong() throws ConversionException {
		if ( isEmpty()) {
			return null;
		} else {
			try {
				return getBigDecimal().longValueExact();
			} catch (ArithmeticException ex) {
				throw new ConversionException(ex);
			}
		}
	}

	public class DefaultValidationRule implements ValidationRule {
		public ValidationState validate() {
			if ( isEmpty() ) {
				return null;
			}
			try {
				long value = getLong();
				if ( value < minValue) {
					return new ValidationState("This value is too small"); // TODO (mk) i18n
				}
				if ( value > maxValue) {
					return new ValidationState("This value is too big"); // TODO (mk) i18n
				}
				return null;
			} catch (ConversionException e) {
				return new ValidationState("Not an integer number"); // TODO (mk) i18n
			}
		}
	}
}