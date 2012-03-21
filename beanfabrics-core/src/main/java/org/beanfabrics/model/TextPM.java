/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ResourceBundle;

import org.beanfabrics.event.OptionsEvent;
import org.beanfabrics.event.OptionsListener;
import org.beanfabrics.util.ResourceBundleFactory;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * The <code>TextPM</code> is a presentation model for a text value.
 * <p>
 * The default value is an empty string.
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
        getValidator().add(new DefaultOptionsValidationRule());
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
        if (equals(text, aText)) {
            return;
        }
        String old = text;
        text = aText;
        revalidate();
        updateModified();
        getPropertyChangeSupport().firePropertyChange("text", old, text);
    }

    private void updateModified() {
        Boolean old = modified;
        modified = (text == null && defaultText == null) || !(text != null && text.equals(defaultText));
        getPropertyChangeSupport().firePropertyChange("modified", old, modified);
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
     * <p>
     * <b>Note:</b> Setting this default value does does not necessarily mean
     * that the actual value of this PM is changed to that value also.
     * 
     * @param aText the text to set as default value
     */
    protected void setDefaultText(String aText) {
        if (aText == null) {
            aText = "";
        }
        String old = defaultText;
        defaultText = aText;
        updateModified();
        revalidate();
        getPropertyChangeSupport().firePropertyChange("defaultText", old, defaultText);
    }

    public boolean isRestrictedToOptions() {
        return restrictedToOptions;
    }

    /**
     * If set to <code>true</code> this <code>TextPM</code> is only valid if
     * its text content is contained in its options.
     * 
     * @param restrictedToOptions
     */
    public void setRestrictedToOptions(boolean restrictedToOptions) {
        if (this.restrictedToOptions == restrictedToOptions) {
            return;
        }
        this.restrictedToOptions = restrictedToOptions;
        revalidate();
        getPropertyChangeSupport().firePropertyChange("restrictedToOptions", !this.restrictedToOptions, this.restrictedToOptions); //$NON-NLS-1$
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        String text = this.getText();
        return text == null || text.trim().length() == 0;
        // TODO (mk) think about trimming
    }

    /** {@inheritDoc} */
    public boolean isModified() {
        return modified;
    }

    /** {@inheritDoc} */
    public void reset() {
        setText(getDefaultText());
    }

    /** {@inheritDoc} */
    public void preset() {
        setDefaultText(this.getText());
    }

    /** {@inheritDoc} */
    public void reformat() {

    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options newOptions) {
        if (options == newOptions) {
            return;
        }
        Options old = options;
        if (options != null) {
            options.removeOptionsListener(optionsListener);
        }
        options = newOptions;

        if (options != null) {
            options.addOptionsListener(optionsListener);
        }
        revalidate();
        getPropertyChangeSupport().firePropertyChange("options", old, options);
    }

    /** {@inheritDoc}
     * <p>
     * The default implementation returns a {@link TextComparable} or one of its subclasses.
     *  */
    @Override
    public Comparable<?> getComparable() {
        return new TextComparable();
    }

    /**
     * The {@link TextComparable} delegates the comparison to a {@link CollationKey} constructed
     * from the PM's text using a {@link Collator}.
     * 
     * @author Michael Karneim
     */
    protected class TextComparable implements Comparable {
        CollationKey key;

        /**
         * Constructs a {@link TextComparable} using the locale-sensitve default {@link Collator}.
         */
        public TextComparable() {
            this(Collator.getInstance());
        }
        /**
         * Constructs a {@link TextComparable} using the given {@link Collator}.
         * @param collator
         */
        public TextComparable(Collator collator) {
            key = collator.getCollationKey(TextPM.this.text);
        }

        /** {@inheritDoc} */
        public int compareTo(Object o) {
            if (!(o instanceof TextComparable)) {
                throw new IllegalArgumentException("o must be instance of" + TextComparable.class);
            }
            TextComparable oc = (TextComparable)o;
            return key.compareTo(oc.key);
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
            TextComparable oc = (TextComparable)o;

            return key.equals(oc.key);
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }

    private final OptionsListener optionsListener = new OptionsListener() {
        public void changed(OptionsEvent evt) {
            revalidate();
            getPropertyChangeSupport().firePropertyChange("options", null, null);
        }
    };

    public class DefaultOptionsValidationRule implements ValidationRule {
        public ValidationState validate() {
            if (isRestrictedToOptions() && getOptions() != null && !isEmpty()) {
                if (getOptions().containsValue(text) == false) {
                    String message = resourceBundle.getString(KEY_MESSAGE_VALUE_NOT_AN_OPTION);
                    return new ValidationState(message);
                }
            }
            return null;
        }
    }
}