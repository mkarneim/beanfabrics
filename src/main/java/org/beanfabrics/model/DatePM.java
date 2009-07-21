/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import org.beanfabrics.util.ResourceBundleFactory;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * The DatePM is a presentation model for {@link Date} objects. The text format
 * is locale dependent but can be changed manually by calling
 * {@link #setFormat(DateFormat)}.
 * 
 * @author Max Gensthaler
 * @author Michael Karneim
 */
public class DatePM extends TextPM implements IDatePM {
    protected static final String KEY_MESSAGE_INVALID_DATE = "message.invalidDate";
    private final ResourceBundle resourceBundle = ResourceBundleFactory.getBundle(DatePM.class);
    private DateFormat format;

    /**
     * Constructs a new instance with the default format (just showing the date,
     * not the time).
     */
    public DatePM() {
        this.setFormat(this.createDefaultFormat());
        this.getValidator().add(new DefaultValidationRule());
    }

    /** {@inheritDoc} */
    public DateFormat getFormat() {
        return format;
    }

    /** {@inheritDoc} */
    public void setFormat(DateFormat newFormat) {
        Format oldFormat = this.format;
        if (oldFormat == newFormat) {
            return;
        }
        this.format = newFormat;
        this.revalidate();
        this.getPropertyChangeSupport().firePropertyChange("format", oldFormat, newFormat); //$NON-NLS-1$
    }

    /**
     * This returns a localized {@link DateFormat} for converting a {@link Date}
     * to a {@link String} and vice versa.
     */
    protected DateFormat createDefaultFormat() {
        return getDateFormat(Locale.getDefault());
    }

    /** {@inheritDoc} */
    public Date getDate()
        throws ConversionException {
        try {
            if (this.isEmpty()) {
                return null;
            }
            String str = getText();
            Date result = (Date)format.parseObject(str);
            return result;
        } catch (ParseException e) {
            throw new ConversionException(e);
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

    public Comparable getComparable() {
        return new DateComparable();
    }

    private class DateComparable extends TextComparable {
        private Long time;

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
        public int compareTo(Object o) {
            if (o == null) {
                throw new IllegalArgumentException("o == null");
            }
            if (!(o instanceof DateComparable)) {
                throw new IllegalArgumentException("incompatible comparable class");
            }
            DateComparable oc = (DateComparable)o;
            if (this.time == null) {
                if (oc.time == null) {
                    return super.compareTo(o);
                } else {
                    return -1;
                }
            } else {
                if (oc.time == null) {
                    return 1;
                } else {
                    return this.time.compareTo(oc.time);
                }
            }
        }
    }

    protected class DefaultValidationRule implements ValidationRule {
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

    public static DateFormat getDateFormat(Locale locale) {
        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        format.setLenient(false);
        return format;
    }
}