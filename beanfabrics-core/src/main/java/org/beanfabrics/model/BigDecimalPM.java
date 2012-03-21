/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.ResourceBundle;

import org.beanfabrics.util.ResourceBundleFactory;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * The {@link BigDecimalPM} is a {@link PresentationModel} that contains a
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
    private IFormat<BigDecimal> format;

    /**
     * Constructs a {@link BigDecimalPM}.
     */
    public BigDecimalPM() {
        format = createDefaultFormat();
        // Please note: to disable default validation rules just call getValidator().clear();
        getValidator().add(new BigDecimalValidationRule());
    }

    /**
     * Reformats the text value by first parsing it and the formatting it using
     * this PM's format.
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
     * constructor and usually returns a {@link BigDecimalPM.Format} with the
     * default {@link DecimalFormat} of the current {@link Locale}.
     *
     * @return the default format for this PM
     */
    protected IFormat<BigDecimal> createDefaultFormat() {
        return new Format(getDecimalFormat(Locale.getDefault()));
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
     * Returns the {@link IFormat} of this PM used for converting between
     * {@link BigDecimal} and {@link String} values.
     *
     * @return the format
     */
    public IFormat<BigDecimal> getFormat() {
        return format;
    }

    /**
     * Sets the format of this PM and reformats the text value. The format is
     * used for converting between {@link BigDecimal} and {@link String} values.
     *
     * @param newFormat the new format
     */
    public void setFormat(IFormat<BigDecimal> newFormat) {
        if (newFormat == null) {
            throw new IllegalArgumentException("newFormat == null");
        }
        IFormat<BigDecimal> oldFormat = format;
        if (oldFormat == newFormat) {
            return;
        }
        boolean doReformat;
        BigDecimal oldValue = null;
        try {
            oldValue = getBigDecimal();
            doReformat = true;
        } catch (ConversionException ex) {
            doReformat = false;
        }
        format = newFormat;
        revalidate();
        getPropertyChangeSupport().firePropertyChange("format", oldFormat, newFormat); //$NON-NLS-1$
        if (doReformat) {
            setBigDecimal(oldValue);
        }
    }

    /** {@inheritDoc} */
    public void setBigDecimal(BigDecimal value) {
        if (value == null) {
            setText(null);
        } else {
            String str = format.format(value);
            setText(str);
        }
    }

    /** {@inheritDoc} */
    public BigDecimal getBigDecimal()
        throws ConversionException {
        if (isEmpty()) {
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
        return format.parse(text);
    }

    /**
     * This rule evaluates to invalid if the PM's value can't be converted into
     * a {@link BigDecimal}.
     *
     * @author Michael Karneim
     */
    public class BigDecimalValidationRule implements ValidationRule {
        /** {@inheritDoc} */
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
    public Comparable<?> getComparable() {
        return new BigDecimalComparable();
    }

    /**
     * The {@link BigDecimalComparable} delegates the comparison to the model's
     * BigDecimal value if available, or otherwise falls back to the
     * <code>super.compareTo(...)</code>.
     *
     * @author Michael Karneim
     */
    protected class BigDecimalComparable extends TextComparable {
        BigDecimal bd;

        /**
         * Constructs a {@link BigDecimalComparable}.
         */
        public BigDecimalComparable() {
            if (!isEmpty()) {
                try {
                    bd = getBigDecimal();
                } catch (ConversionException ex) {
                    // ignore
                    bd = null;
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

            if (bd == null) {
                if (oc.bd == null) {
                    return super.compareTo(o);
                } else {
                    return -1;
                }
            } else {
                if (oc.bd == null) {
                    return 1;
                } else {
                    return bd.compareTo(oc.bd);
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
            return ((bd == null ? castedObj.bd == null : bd.equals(castedObj.bd)));
        }

        @Override
        public int hashCode() {
        	return bd.hashCode();
        }
    }

    /**
     * The {@link BigDecimalPM.Format} is a {@link IFormat} for converting
     * between {@link BigDecimal} and {@link String}.
     */
    public static class Format implements IFormat<BigDecimal> {
        private static final String ALLOWED_SYMBOLS = "0#.,E;-";

        private final DecimalFormat strictFormat;
        private final DecimalFormat simplifiedFormat;

        /**
         * Creates a {@link BigDecimalPM.Format} using the given
         * {@link DecimalFormat}.
         *
         * @param format
         */
        public Format(DecimalFormat format) {
            strictFormat = (DecimalFormat)format.clone();
            strictFormat.setParseBigDecimal(true);
            simplifiedFormat = createSimplifiedNumberFormat(strictFormat);
        }

        /**
         * Creates a {@link BigDecimalPM.Format} using the given two formats.
         *
         * @param aStrictFormat the strict format defines, how to format a
         *            BigDecimal into a String and how to parse it
         * @param aSimplifiedFormat the simplified format defines, how to parse
         *            a String if the strict format fails to parse it
         */
        public Format(DecimalFormat aStrictFormat, DecimalFormat aSimplifiedFormat) {
            strictFormat = (DecimalFormat)aStrictFormat.clone();
            strictFormat.setParseBigDecimal(true);
            simplifiedFormat = (DecimalFormat)aSimplifiedFormat.clone();
            simplifiedFormat.setParseBigDecimal(true);
        }

        public DecimalFormat getStrictFormat() {
            return strictFormat;
        }

        public DecimalFormat getSimplifiedFormat() {
            return simplifiedFormat;
        }

        /** {@inheritDoc} */
        public BigDecimal parse(String text)
            throws ConversionException {
            BigDecimal result;
            try {
                result = convert(strictFormat, text);
            } catch (ConversionException ex) {
                result = convert(simplifiedFormat, text);
            }
            return result;
        }

        /** {@inheritDoc} */
        public String format(BigDecimal value) {
            if (value == null) {
                return null;
            } else {
                String result = strictFormat.format(value);
                return result;
            }
        }

        /**
         * Converts the given text into a {@link BigDecimal}.
         *
         * @param text
         * @return a BigDecimal representation of the given text
         * @throws ConversionException if the text value can't be converted into
         *             a valid {@link BigDecimal}
         */
        private BigDecimal convert(DecimalFormat format, String text)
            throws ConversionException {
            if (format.isParseBigDecimal() == false) {
                throw new IllegalStateException("format must parse BigDecimal");
            }
            text = text.trim();
            if (text == null || text.length() == 0) {
                return null;
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
         * Creates a simplified version of the given format. A format is
         * 'simplified' if it contains only those formatting symbols that have
         * to do with the numeric representation. Any literals are removed.
         *
         * @param aFormat
         * @return a simplified version of the given format
         */
        public DecimalFormat createSimplifiedNumberFormat(DecimalFormat aFormat) {
            // replace all 'bad' characters
            String pattern = aFormat.toPattern();
            StringBuilder builder = new StringBuilder();

            boolean escaped = false;
            for (char c : pattern.toCharArray()) {
                if (escaped) {
                    escaped = false;
                    continue;
                }
                if (c == '\'') {
                    escaped = true;
                    continue;
                }
                if (ALLOWED_SYMBOLS.indexOf(c) > -1) {
                    builder.append(c);
                }
            }

            // This can never happen since DecimalFormat throws an IllegalArgumentException: MalformedPattern
            assert escaped == false : "Apostrophe must not be the last character in pattern: " + pattern;

            String simplifiedPattern = builder.toString();

            DecimalFormat simplifiedFormat = (DecimalFormat)aFormat.clone();
            simplifiedFormat.applyPattern(simplifiedPattern);
            return simplifiedFormat;
        }

    }
}
