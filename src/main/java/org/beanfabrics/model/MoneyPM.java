/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;


/**
 * The MoneyPM is a presentation model for a monetary value with a currency format.
 *
 * @author Michael Karneim
 */
// TODO (mk) move this class to beanfabrics-ext.jar
public class MoneyPM extends BigDecimalPM {
	/**
	 * The simplified format is used by the parsing routine when the standard format fails.
	 */
	private DecimalFormat simplifiedFormat;

	public MoneyPM() {
		this.setSimplifiedFormat(this.createSimplifiedFormat());
	}

	public BigDecimal getBigDecimal() throws ConversionException {
		try {
			if (this.isEmpty()) {
				return null;
			}
			String str = getText();
			BigDecimal result;
			try {
				result = (BigDecimal) getFormat().parseObject(str);
			} catch ( ParseException e) {
				result = (BigDecimal) getSimplifiedFormat().parseObject(str);
			}
			return result;
		} catch (ParseException e) {
			throw new ConversionException(e);
		}
	}

	public DecimalFormat getSimplifiedFormat() {
		return simplifiedFormat;
	}

	public void setSimplifiedFormat(DecimalFormat newFormat) {
		Format old = this.simplifiedFormat;
		if (old == newFormat) {
			return;
		}
		this.simplifiedFormat = newFormat;
		this.revalidate();
		this.getPropertyChangeSupport().firePropertyChange(
				"simplifiedFormat", old, newFormat); //$NON-NLS-1$
	}

	protected DecimalFormat createDefaultFormat() {
		return getCurrencyFormat(Locale.getDefault());
	}

	protected DecimalFormat createSimplifiedFormat() {
		return getDecimalFormat(Locale.getDefault());
	}

	public static DecimalFormat getCurrencyFormat(Locale locale) {
		DecimalFormat result = (DecimalFormat)NumberFormat.getCurrencyInstance(locale);
		result.setParseBigDecimal(true);
		return result;
	}
}