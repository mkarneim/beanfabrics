/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.model;

import java.math.BigInteger;


/**
 * @author Michael Karneim
 */
public interface IIntegerPM extends ITextPM {
	void setByte(Byte value);
	Byte getByte() throws ConversionException;
	void setShort(Short value);
	Short getShort() throws ConversionException;
	void setInteger(Integer value);
	Integer getInteger() throws ConversionException;
	void setLong(Long value);
	Long getLong() throws ConversionException;
	void setBigInteger( BigInteger value);
	BigInteger getBigInteger() throws ConversionException;

	void setMinValue(long minValue);
	long getMinValue();
	void setMaxValue(long maxValue);
	long getMaxValue();
}