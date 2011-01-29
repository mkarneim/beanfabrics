/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import org.beanfabrics.util.ResourceBundleFactory;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * The {@link DatePM} is a {@link PresentationModel} that contains a
 * {@link Date} value.
 * <p>
 * The date format used for formatting and pasring can be set by calling
 * {@link #setFormat(DateFormat)}. The default text format is {@link Locale}
 * dependent.
 *
 * @author Michael Karneim
 * @author Max Gensthaler
 */
public class DatePM extends TextPM implements IDatePM {
    protected static final String KEY_MESSAGE_INVALID_DATE = "message.invalidDate";
    private final ResourceBundle resourceBundle = ResourceBundleFactory.getBundle(DatePM.class);

    /**
     * The {@link DateFormatProvider} is a factory for the default
     * {@link DateFormat} used by newly created {@link DatePM} instances.
     *
     * @see DatePM#setDefaultDateFormatProvider(DateFormatProvider)
     */
    public static class DateFormatProvider {
        /**
         * Creates and returns a new {@link DateFormat}
         *
         * @return a new {@link DateFormat}
         */
        public DateFormat getDateFormat() {
            DateFormat format = DateFormat.getDateInstance();
            format.setLenient(false);
            return format;
        }
    }

    private static DateFormatProvider DEFAULT_DATE_FORMAT_PROVIDER = new DateFormatProvider();

    /**
     * Returns the {@link DateFormatProvider} that is used to create the default
     * {@link DateFormat} used by newly created {@link DatePM} instances.
     *
     * @return the {@link DateFormatProvider}
     */
    public static synchronized DateFormatProvider getDefaultDateFormatProvider() {
        return DEFAULT_DATE_FORMAT_PROVIDER;
    }

    /**
     * Sets the {@link DateFormatProvider} that is used to create the default
     * {@link DateFormat} used by newly created {@link DatePM} instances.
     *
     * @param provider
     */
    public static synchronized void setDefaultDateFormatProvider(DateFormatProvider provider) {
        DEFAULT_DATE_FORMAT_PROVIDER = provider;
    }

    private DateFormat format;

    /**
     * Constructs a {@link DatePM} using the default format.
     *
     * @see #getDefaultFormat()
     */
    public DatePM() {
        setFormat(getDefaultFormat());
        // Please note: to disable default validation rules just call getValidator().clear();
        getValidator().add(new DateValidationRule());
    }

    /**
     * Returns a {@link DateFormat} for formatting a {@link Date} to a
     * {@link String} and converting a <code>String</code> to a
     * <code>Date</code>.
     *
     * @return a localized {@link DateFormat} for formatting and parsing this
     *         PM's value
     */
    protected DateFormat getDefaultFormat() {
        return getDefaultDateFormatProvider().getDateFormat();
    }

    /** {@inheritDoc} */
    public DateFormat getFormat() {
        return format;
    }

    /** {@inheritDoc} */
    public void setFormat(DateFormat newFormat) {
        Format oldFormat = format;
        if (oldFormat == newFormat) {
            return;
        }
        boolean doReformat;
        Date oldValue = null;
        try {
            oldValue = getDate();
            doReformat = true;
        } catch (ConversionException ex) {
            doReformat = false;
        }
        format = (DateFormat)newFormat.clone();
        getPropertyChangeSupport().firePropertyChange("format", oldFormat, newFormat); //$NON-NLS-1$
        revalidate();
        if (doReformat) {
            setDate(oldValue);
        }
    }

    /** {@inheritDoc} */
    public Date getDate()
        throws ConversionException {

        if (isEmpty()) {
            return null;
        }
        String str = getText();
        ParsePosition posInOut = new ParsePosition(0);
        Date result = (Date)format.parseObject(str, posInOut);
        if (posInOut.getIndex() == str.length()) {
            return result;
        } else {
            throw new ConversionException();
        }

    }

    /** {@inheritDoc} */
    public void setDate(Date date) {
        if (date == null) {
            setText(null);
        } else {
            String str = format.format(date);
            setText(str);
        }
    }

    /**
     * Sets the default value of this PM to the given {@link Date} value.
     *
     * @param date the default value
     */
    public void setDefaultDate(Date date) {
        if (date == null) {
            setDefaultText(null);
        } else {
            setDefaultText(format.format(date));
        }
    }

    /** {@inheritDoc} */
    @Override
	public Comparable<?> getComparable() {
        return new DateComparable();
    }

    /**
     * The {@link DateComparable} delegates the comparison to the PM's date
     * value.
     *
     * @author Michael Karneim
     */
    private class DateComparable extends TextComparable {
        private Long time;

        /**
         * Constructs a {@link DateComparable}.
         */
        public DateComparable() {
            if (!isEmpty()) {
                try {
                    Date date = getDate();
                    time = date.getTime();
                } catch (ConversionException ex) {
                    // ignore
                }
            }
        }

        /** {@inheritDoc} */
        @Override
		public int compareTo(Object o) {
            if (o == null) {
                throw new IllegalArgumentException("o == null");
            }
            if (!(o instanceof DateComparable)) {
                throw new IllegalArgumentException("incompatible comparable class");
            }
            DateComparable oc = (DateComparable)o;
            if (time == null) {
                if (oc.time == null) {
                    return super.compareTo(o);
                } else {
                    return -1;
                }
            } else {
                if (oc.time == null) {
                    return 1;
                } else {
                    return time.compareTo(oc.time);
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
            DateComparable castedObj = (DateComparable)o;
            return ((time == null ? castedObj.time == null : time.equals(castedObj.time)));
        }

        @Override
        public int hashCode() {
        	return time.hashCode();
        }
    }

    /**
     * This rule evaluates to invalid if the PM's value can't be converted into
     * a {@link Date}.
     *
     * @author Michael Karneim
     */
    public class DateValidationRule implements ValidationRule {
        /** {@inheritDoc} */
        public ValidationState validate() {
            if (isEmpty()) {
                return null;
            }
            try {
                getDate(); // try to convert to date
                return null;
            } catch (ConversionException ex) {
                String message = resourceBundle.getString(KEY_MESSAGE_INVALID_DATE);
                return new ValidationState(message);
            }
        }
    }

}
