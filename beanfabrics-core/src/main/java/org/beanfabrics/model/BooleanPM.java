/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import org.beanfabrics.util.ResourceBundleFactory;

/**
 * The {@link BooleanPM} is a {@link PresentationModel} for a {@link Boolean}
 * value.
 * <p>
 * The default value is {@link Boolean#FALSE}.
 * <p>
 * The text representation of the boolean value is localized depending on the
 * default {@link Locale}.
 * 
 * @author Michael Karneim
 */
public class BooleanPM extends TextPM implements IBooleanPM {
    protected static final String KEY_TEXT_TRUE = "text.true";
    protected static final String KEY_TEXT_FALSE = "text.false";

    private final ResourceBundle resourceBundle = ResourceBundleFactory.getBundle(BooleanPM.class);

    /**
     * Constructs a BooleanPM.
     */
    public BooleanPM() {
        this.setOptions(createDefaultOptions());
        this.setBoolean(false);
        this.setRestrictedToOptions(true);
        this.setMandatory(true);
    }

    /** {@inheritDoc} */
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
    public Boolean getBoolean()
        throws ConversionException {
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

    /**
     * Sets the default value of this PM to the given {@link Boolean} value.
     * 
     * @param value
     */
    public void setDefaultBoolean(Boolean value) {
        if (value == null) {
            this.setDefaultText(null);
        } else {
            Options<Boolean> options = this.getOptions();
            String txt = options.get(value);
            this.setDefaultText(txt);
        }
    }

    /**
     * Creates the default {@link Options} attribute of this PM. The method is
     * called from the constructor.
     * 
     * @return the default {@link Options}
     */
    protected Options<Boolean> createDefaultOptions() {
        Options<Boolean> result = new Options<Boolean>();
        result.put(true, resourceBundle.getString(KEY_TEXT_TRUE));
        result.put(false, resourceBundle.getString(KEY_TEXT_FALSE));
        return result;
    }
}
