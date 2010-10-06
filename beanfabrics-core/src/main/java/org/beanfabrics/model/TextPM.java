/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.util.ResourceBundle;

import org.beanfabrics.event.OptionsEvent;
import org.beanfabrics.event.OptionsListener;
import org.beanfabrics.util.ResourceBundleFactory;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * The <code>TextPM</code> is a presentation model for a text value.
 * <p>
 * The default value is a empty string.
 * 
 * @author Michael Karneim
 */
public class TextPM extends AbstractValuePM implements ITextPM {
    protected static final String KEY_MESSAGE_VALUE_NOT_AN_OPTION = "message.not_an_option";
    private final ResourceBundle resourceBundle = ResourceBundleFactory.getBundle(TextPM.class);
    /**
     * The text content of this <code>TextPM</code>. It must never be
     * <code>null</code>.
     */
    private String text = "";
    private String defaultText = "";
    private boolean modified = false;
    private Options options;
    private boolean restrictedToOptions = true;

    /**
     * Constructs a new <code>TextPM<code>.
     */
    public TextPM(String initialText) {
        this();
        setText(initialText);
    }

    /**
     * Constructs a new <code>TextPM<code>.
     */
    public TextPM() {
        this.getValidator().add(new DefaultOptionsValidationRule());
    }

    /** {@inheritDoc} */
    public String getText() {
        return text;
    }

    // TODO (mk) remove this method
    public String getText(boolean convertWhitespaceStringToNull) {
        if (text.trim().length() == 0) {
            return null;
        } else {
            return text;
        }
    }

    /** {@inheritDoc} */
    public void setText(String aText) {
        if (aText == null) {
            aText = "";
        }
        if (equals(this.text, aText)) {
            return;
        }
        String old = this.text;
        this.text = aText;
        this.revalidate();
        this.updateModified();
        this.getPropertyChangeSupport().firePropertyChange("text", old, this.text);
    }

    private void updateModified() {
        Boolean old = this.modified;
        this.modified = (this.text == null && this.defaultText == null) || !(this.text != null && this.text.equals(this.defaultText));
        this.getPropertyChangeSupport().firePropertyChange("modified", old, this.modified);
    }

    /**
     * Get the default text value of this model.
     * 
     * @return the default text value
     */
    protected String getDefaultText() {
        return defaultText;
    }

    /**
     * Sets the default text value of this model.
     * <p>
     * Use {@link #reset()} to set the model's value to it's default value or
     * {@link #preset()} to set the model's default value to it's current value.
     * 
     * @param aText the text to set as default value
     */
    protected void setDefaultText(String aText) {
        if (aText == null) {
            aText = "";
        }
        String old = this.defaultText;
        this.defaultText = aText;
        this.updateModified();
        this.revalidate();
        this.getPropertyChangeSupport().firePropertyChange("defaultText", old, this.defaultText);
    }

    public boolean isRestrictedToOptions() {
        return restrictedToOptions;
    }

    /**
     * If set to <code>true</code> this <code>TextPM</code> is only valid if
     * it's text content is contained in it's options.
     * 
     * @param restrictedToOptions
     */
    public void setRestrictedToOptions(boolean restrictedToOptions) {
        if (this.restrictedToOptions == restrictedToOptions) {
            return;
        }
        this.restrictedToOptions = restrictedToOptions;
        this.revalidate();
        this.getPropertyChangeSupport().firePropertyChange("restrictedToOptions", !this.restrictedToOptions, this.restrictedToOptions); //$NON-NLS-1$
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        String text = this.getText();
        return text == null || text.trim().length() == 0;
        // TODO (mk) think about trimming
    }

    /** {@inheritDoc} */
    public boolean isModified() {
        return this.modified;
    }

    /** {@inheritDoc} */
    public void reset() {
        this.setText(this.getDefaultText());
    }

    /** {@inheritDoc} */
    public void preset() {
        this.setDefaultText(this.getText());
    }

    /** {@inheritDoc} */
    public void reformat() {

    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options newOptions) {
        if (this.options == newOptions) {
            return;
        }
        Options old = this.options;
        if (this.options != null) {
            this.options.removeOptionsListener(optionsListener);
        }
        this.options = newOptions;

        if (this.options != null) {
            this.options.addOptionsListener(optionsListener);
        }
        revalidate();
        this.getPropertyChangeSupport().firePropertyChange("options", old, this.options);
    }

    public Comparable getComparable() {
        return new TextComparable();
    }

    protected class TextComparable implements Comparable {
        String text;

        public TextComparable() {
            this.text = TextPM.this.text.toLowerCase();
        }

        /** {@inheritDoc} */
        public int compareTo(Object o) {
            if (!(o instanceof TextComparable)) {
                throw new IllegalArgumentException("o must be instance of" + TextComparable.class);
            }
            TextComparable oc = (TextComparable)o;
            return this.text.compareTo(oc.text);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (o.getClass() != getClass()) {
                return false;
            }
            TextComparable castedObj = (TextComparable)o;
            return ((this.text == null ? castedObj.text == null : this.text.equals(castedObj.text)));
        }
    }

    private OptionsListener optionsListener = new OptionsListener() {
        public void changed(OptionsEvent evt) {
            revalidate();
            getPropertyChangeSupport().firePropertyChange("options", null, null);
        }
    };

    public class DefaultOptionsValidationRule implements ValidationRule {
        public ValidationState validate() {
            if (isRestrictedToOptions() && getOptions() != null && !isEmpty()) {
                if (getOptions().containsValue(text) == false) {
                    final String message = resourceBundle.getString(KEY_MESSAGE_VALUE_NOT_AN_OPTION);
                    return new ValidationState(message);
                }
            }
            return null;
        }
    }
}