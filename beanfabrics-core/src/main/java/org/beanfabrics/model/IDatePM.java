/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The {@link IDatePM} is the interface of {@link PresentationModel} components
 * that contain a {@link Date} value.
 * <p>
 * The date format used for formatting and pasring can be set by calling
 * {@link #setFormat(DateFormat)}. The default text format is {@link Locale}
 * dependent.
 * 
 * @author Michael Karneim
 * @author Max Gensthaler
 */
public interface IDatePM extends ITextPM {
    /**
     * Returns the date format used for parsing and converting between the text
     * and date value. This format is used for validation also.
     * 
     * @return the date format used for parsing and converting
     */
    public DateFormat getFormat();

    /**
     * Sets the date format used for parsing and converting between the text and
     * date value. This format is used for validation also.
     * <p>
     * For example, to set a format with date and time component do:
     * 
     * <pre>
     * DateFormat format = DateFormat.getDateTimeInstance();
     * format.setLenient(false);
     * pm.setFormat(format);
     * </pre>
     * <P>
     * Implementors must try to reformat the content to match the new format.
     * 
     * @param newFormat the new format
     */
    public void setFormat(DateFormat newFormat);

    /**
     * Returns the value of this PM as a {@link Date}.
     * 
     * @return the value of this PM as a Date
     * @throws ConversionException if the text value of this PM cannot be
     *             converted to a {@link Date} using the defined format
     */
    public Date getDate()
        throws ConversionException;

    /**
     * Sets the value of this PM to the given {@link Date}.
     * 
     * @param date the date value
     */
    public void setDate(Date date);
}