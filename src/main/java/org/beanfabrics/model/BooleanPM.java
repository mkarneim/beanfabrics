/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.model;

import java.util.NoSuchElementException;
import java.util.ResourceBundle;

/**
 * The <code>BooleanPM</code> is a presentation model for a {@link Boolean} value.
 * <p>
 * The default value is <code>false</code>.
 *
 * @author Michael Karneim
 */
public class BooleanPM extends TextPM implements IBooleanPM {
	protected static final String KEY_TEXT_TRUE = "text.true";
	protected static final String KEY_TEXT_FALSE = "text.false";

	private final ResourceBundle resourceBundle = ResourceBundle.getBundle(BooleanPM.class.getName() + "ResourceBundle");

	public BooleanPM() {
		this.setOptions(createDefaultOptions());
		this.setBoolean(false);
		this.setRestrictedToOptions(true);
		this.setMandatory(true);
	}

	public void setBoolean(Boolean b) {
		if (b == null) {
			this.setText(null);
		} else {
			Options<Boolean> options = this.getOptions();
			String txt = options.get(b);
			this.setText(txt);
		}
	}

	/** {@inheritDoc} */
	public Boolean getBoolean() throws ConversionException {
		if (this.isEmpty()) {
			return null;
		}
		String strVal = this.getText();
		Options<Boolean> options = this.getOptions();
		try {
			return options.getKey(strVal);
		} catch (NoSuchElementException ex) {
			throw new ConversionException("strVal='" + strVal + "' is not a boolean", ex);
		}
	}

	protected Options<Boolean> createDefaultOptions() {
		Options<Boolean> result = new Options<Boolean>();
		result.put(true, resourceBundle.getString(KEY_TEXT_TRUE));
		result.put(false, resourceBundle.getString(KEY_TEXT_FALSE));
		return result;
	}
}