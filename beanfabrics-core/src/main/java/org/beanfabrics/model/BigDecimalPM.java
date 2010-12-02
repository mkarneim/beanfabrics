/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.ResourceBundle;

import org.beanfabrics.util.ResourceBundleFactory;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * The {@link BigDecimalPM} is a {@link PresentationModel} for a
 * {@link BigDecimal} value.
 * <p>
 * This class is the base class for any other 'numeric' presentation models in
 * Beanfabrics since it has the most generic validation rule (see
 * {@link BigDecimalValidationRule}) and content conversion methods.
 * 
 * @author Michael Karneim
 */
public class BigDecimalPM extends TextPM implements IBigDecimalPM {
    private static final String KEY_MESSAGE_INVALID_NUMBER = "message.invalidNumber";
    private final ResourceBundle resourceBundle = ResourceBundleFactory.getBundle(BigDecimalPM.class);
    private DecimalFormat format;

    /**
     * Constructs a {@link BigDecimalPM}.
     */
    public BigDecimalPM() {
        this.setFormat(this.createDefaultFormat());
        // Please note: to disable default validation rules just call getValidator().clear();
        this.getValidator().add(new BigDecimalValidationRule());
    }

    /**
     * Parses the content and formats it according to this model's format.
     * 
     * @see #setFormat(DecimalFormat)
     */
    @Override
    public void reformat() {
        try {
            setBigDecimal(getBigDecimal());
        } catch (ConversionException ex) {
            // ignore
        }
    }

    /**
     * Creates the default format for this PM. This method is called from the
     * constructor and usually returns a {@link DecimalFormat} for the current
     * {@link Locale}.
     * 
     * @return the default format for this PM
     */
    protected DecimalFormat createDefaultFormat() {
        return getDecimalFormat(Locale.getDefault());
    }

    /**
     * Factory method for creating a {@link DecimalFormat} for the specified
     * {@link Locale}.
     * 
     * @param locale
     * @return the new {@link DecimalFormat}.
     */
    protected static DecimalFormat getDecimalFormat(Locale locale) {
        DecimalFormat result = (DecimalFormat)NumberFormat.getInstance(locale);
        result.setParseBigDecimal(true);
        return result;
    }

    /**
     * Returns the decimal format of this PM.
     * 
     * @return the decimal format
     * @see #reformat()
     */
    public DecimalFormat getFormat() {
        return format;
    }

    /**
     * Sets the decimal format of this PM to the given value. This format will
     * be cloned before use.
     * 
     * @param newFormat the new format for this model
     * @see #reformat()
     */
    public void setFormat(DecimalFormat newFormat) {
        Format old = this.format;
        if (old == newFormat) {
            return;
        }
        DecimalFormat clonedformat = (DecimalFormat)newFormat.clone();
        clonedformat.setParseBigDecimal(true);
        this.format = clonedformat;
        this.revalidate();
        this.getPropertyChangeSupport().firePropertyChange("format", old, newFormat); //$NON-NLS-1$
    }

    /**
     * Sets the value of this PM to the given {@link BigDecimal} value.
     * 
     * @param value
     * @see #setText(String)
     */
    public void setBigDecimal(BigDecimal value) {
        if (value == null) {
            setText(null);
        } else {
            String str = format.format(value);
            setText(str);
        }
    }

    /**
     * Returns the value of this PM as a {@link BigDecimal}.
     * 
     * @return the value of this PM as a {@link BigDecimal}
     * @throws ConversionException if the text value can't be converted into a
     *             valid {@link BigDecimal}
     */
    public BigDecimal getBigDecimal()
        throws ConversionException {
        if (this.isEmpty()) {
            return null;
        }
        String str = getText();

        BigDecimal result = convert(str);
        return result;
    }

    /**
     * Sets the default value of this PM to the given {@link BigDecimal} value.
     * 
     * @param value
     * @see #setDefaultText(String)
     */
    public void setDefaultBigDecimal(BigDecimal value) {
        if (value == null) {
            setDefaultText(null);
        } else {
            setDefaultText(format.format(value));
        }
    }

    /**
     * Sets the value of this PM to the giben {@link BigInteger} value.
     * 
     * @param value
     * @see #setText(String)
     */
    public void setBigInteger(BigInteger value) {
        if (value == null) {
            setBigDecimal(null);
        } else {
            setBigDecimal(new BigDecimal(value));
        }
    }

    /**
     * Returns the value of this PM as a {@link BigInteger}.
     * 
     * @return the value of this PM as a {@link BigInteger}
     * @throws ConversionException if the text value can't be converted into a
     *             valid {@link BigInteger}
     */
    public BigInteger getBigInteger()
        throws ConversionException {
        if (isEmpty()) {
            return null;
        } else {
            try {
                return getBigDecimal().toBigIntegerExact();
            } catch (ArithmeticException ex) {
                throw new ConversionException(ex);
            }
        }
    }

    /**
     * Sets the default value of this PM to the given {@link BigInteger} value.
     * 
     * @param value
     * @see #setDefaultText(String)
     */
    public void setDefaultBigInteger(BigInteger value) {
        if (value == null) {
            setDefaultBigDecimal(null);
        } else {
            setDefaultBigDecimal(new BigDecimal(value));
        }
    }

    /**
     * Converts the given text into a {@link BigDecimal}.
     * 
     * @param text
     * @return a BigDecimal representation of the given text
     * @throws ConversionException if the text value can't be converted into a
     *             valid {@link BigDecimal}
     */
    private BigDecimal convert(String text)
        throws ConversionException {
        if (format.isParseBigDecimal() == false) {
            throw new IllegalStateException("format must parse BigDecimal");
        }
        ParsePosition pos = new ParsePosition(0);
        BigDecimal result = (BigDecimal)format.parse(text, pos);
        if (result != null && pos.getIndex() == text.length()) {
            return result;
        } else {
            throw new ConversionException("Can't convert '" + text + "' to BigDecimal");
        }
    }

    /**
     * This rule evaluates to invalid if the PM's value can't be converted into
     * a {@link BigDecimal}.
     * 
     * @author Michael Karneim
     */
    public class BigDecimalValidationRule implements ValidationRule {
        public ValidationState validate() {
            if (isEmpty()) {
                return null;
            }
            try {
                convert(getText());
                return null;
            } catch (ConversionException ex) {
                String message = resourceBundle.getString(KEY_MESSAGE_INVALID_NUMBER);
                return new ValidationState(message);
            }
        }
    }

    @Override
    public Comparable getComparable() {
        return new BigDecimalComparable();
    }

    private class BigDecimalComparable extends TextComparable {
        BigDecimal bd;

        public BigDecimalComparable() {
            if (!isEmpty()) {
                try {
                    this.bd = getBigDecimal();
                } catch (ConversionException ex) {
                    // ignore
                    this.bd = null;
                }
            }
        }

        @Override
        public int compareTo(Object o) {
            if (o == null) {
                throw new IllegalArgumentException("o==null");
            }
            if (!(o instanceof BigDecimalComparable)) {
                throw new IllegalArgumentException("incompatible comparable class: " + o.getClass().getName());
            }
            BigDecimalComparable oc = (BigDecimalComparable)o;

            if (this.bd == null) {
                if (oc.bd == null) {
                    return super.compareTo(o);
                } else {
                    return -1;
                }
            } else {
                if (oc.bd == null) {
                    return 1;
                } else {
                    return this.bd.compareTo(oc.bd);
                }
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!super.equals(o)) {
                return false;
            }
            if (o == null) {
                return false;
            }
            if (o.getClass() != getClass()) {
                return false;
            }
            BigDecimalComparable castedObj = (BigDecimalComparable)o;
            return ((this.bd == null ? castedObj.bd == null : this.bd.equals(castedObj.bd)));
        }
    }
}
