/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import java.math.BigInteger;

/**
 * THe {@link IIntegerPM} is interface for {@link PresentationModel} classes
 * that contain an numeric integer value.
 * 
 * @author Michael Karneim
 */
public interface IIntegerPM extends ITextPM {
    /**
     * Sets the value if this PM to the given {@link Byte}.
     * 
     * @param value the Byte value
     */
    public void setByte(Byte value);

    /**
     * Returns the value of this PM as a {@link Byte}.
     * 
     * @return the value of this PM as a Byte
     * @throws ConversionException if the text value of this PM cannot be
     *             converted to a {@link Byte}
     */
    public Byte getByte()
        throws ConversionException;

    /**
     * Sets the value of this PM to the given {@link Short}.
     * 
     * @param value the Short value
     */
    public void setShort(Short value);

    /**
     * Returns the value of this PM as a {@link Short}.
     * 
     * @return the value of this PM as a Short
     * @throws ConversionException if the text value of this PM cannot be
     *             converted to a {@link Short}
     */
    public Short getShort()
        throws ConversionException;

    /**
     * Sets the value of this PM to the given {@link Integer}.
     * 
     * @param value the Integer value
     */
    public void setInteger(Integer value);

    /**
     * Returns the value of this PM as an {@link Integer}.
     * 
     * @return the value of this PM as an Integer
     * @throws ConversionException if the text value of this PM cannot be
     *             converted to an {@link Integer}
     */
    public Integer getInteger()
        throws ConversionException;

    /**
     * Sets the value of this PM to the given {@link Long}.
     * 
     * @param value the Long value
     */
    public void setLong(Long value);

    /**
     * Returns the value of this PM as a {@link Long}.
     * 
     * @return the value of this PM as a Long
     * @throws ConversionException if the text value of this PM cannot be
     *             converted to a {@link Long}
     */
    public Long getLong()
        throws ConversionException;

    /**
     * Sets the value of this PM to the given {@link BigInteger}.
     * 
     * @param value the BigInteger value
     */
    public void setBigInteger(BigInteger value);

    /**
     * Returns the value of this PM as a {@link BigInteger}.
     * 
     * @return the value of this PM as a BigInteger
     * @throws ConversionException if the text value of this PM cannot be
     *             converted to a {@link BigInteger}
     */
    public BigInteger getBigInteger()
        throws ConversionException;

    /**
     * Sets the minimum numeric long value that is valid for this PM's value.
     * 
     * @param minValue the minimum value
     * @see #getMinValue()
     */
    public void setMinValue(long minValue);

    /**
     * Returns the minimum numeric value that is valid for this PM's value.
     * <p>
     * Implementors must ensure that the validation of this PM should evaluate
     * to invalid if the numeric value of this PM is less than the specified
     * minimum value.
     * 
     * @return the minimum numeric value
     */
    public long getMinValue();

    /**
     * Sets the maximum numeric long value that is valid for this PM's value.
     * 
     * @param maxValue the maximum value
     * @see #getMaxValue()
     */
    public void setMaxValue(long maxValue);

    /**
     * Returns the maximum numeric value that is valid for this PM's value.
     * <p>
     * Implementors must ensure that the validation of this PM should evaluate
     * to invalid if the numeric value of this PM is greater than the specified
     * maximum value.
     * 
     * @return the maximum numeric value
     */
    public long getMaxValue();
}