/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

/**
 * The {@link IBooleanPM} is the interface for PM components that support a
 * {@link Boolean} value.
 * 
 * @author Michael Karneim
 */
public interface IBooleanPM extends ITextPM {
    /**
     * Sets the value of this PM to the given value.
     * 
     * @param value the new value.
     * @see #setText(String)
     */
    public void setBoolean(Boolean value);

    /**
     * Returns the value of this PM as a {@link Boolean}.
     * 
     * @return the value of this PM as a {@link Boolean}
     * @throws ConversionException if the text value can't be converted into a
     *             valid {@link Boolean}
     */
    public Boolean getBoolean();
}