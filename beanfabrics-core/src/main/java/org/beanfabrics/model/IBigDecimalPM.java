/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import java.math.BigDecimal;

/**
 * The {@link IBigDecimalPM} is the interface fo PM components that support a
 * {@link BigDecimal} value.
 * 
 * @author Michael Karneim
 */
public interface IBigDecimalPM extends ITextPM {
    /**
     * Returns the value of this PM as a {@link BigDecimal}.
     * 
     * @return the value of this PM as a {@link BigDecimal}
     * @throws ConversionException if the text value can't be converted into a
     *             valid {@link BigDecimal}
     */
    public BigDecimal getBigDecimal()
        throws ConversionException;

    /**
     * Sets the value of this PM to the given {@link BigDecimal} value.
     * 
     * @param value
     * @see #setText(String)
     */
    public void setBigDecimal(BigDecimal value);
}