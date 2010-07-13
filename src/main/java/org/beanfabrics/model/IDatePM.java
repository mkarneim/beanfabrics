/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author Michael Karneim
 * @author Max Gensthaler
 */
public interface IDatePM extends ITextPM {
    /**
     * Return the format to be used by this object to check the input and
     * convert the input to a {@link Date} and vice versa.
     */
    DateFormat getFormat();

    /**
     * Set the format to be used by this object to check the input and convert
     * the input to a {@link Date} and vice versa.
     * <p>
     * I.e. to set a format showing date and time do:
     * 
     * <pre>
     * DateFormat format = DateFormat.getDateTimeInstance();
     * format.setLenient(false);
     * setFormat(format);
     * </pre>
     * 
     * @param newFormat format to use for {@link Date}-{@link String} conversion
     */
    void setFormat(DateFormat newFormat);

    /**
     * Return the date represented by this object.
     * 
     * @throws ConversionException thrown if the text of this object cannot be
     *             parsed as date by the given format
     */
    Date getDate()
        throws ConversionException;

    /**
     * Set the {@link Date} to be represented by this object.
     * 
     * @param date {@link Date} to set
     */
    void setDate(Date date);
}