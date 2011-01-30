/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import java.util.ResourceBundle;

import org.beanfabrics.util.ResourceBundleFactory;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * The {@link AbstractValuePM} is the general superclass of PM components that
 * implement the {@link IValuePM} interface and contain some 'value' and other
 * additional attributes which are generally usefull for user interface
 * programming, like title, description, mandatory, and editable.
 *
 * @author Michael Karneim
 */
public abstract class AbstractValuePM extends AbstractPM implements IValuePM {
    protected static final String KEY_MESSAGE_MANDATORY = "message.mandatory";
    private final ResourceBundle resourceBundle = ResourceBundleFactory.getBundle(AbstractValuePM.class);
    private boolean editable = true;
    private boolean mandatory = false;
    private String description = null;
    private String title = null;

    /**
     * Constructs a {@link AbstractValuePM}.
     */
    protected AbstractValuePM() {
        // Please note: to disable default validation rules just call getValidator().clear();
        getValidator().add(new MandatoryValidationRule());
    }

    /** {@inheritDoc} */
    public String getDescription() {
        return description;
    }

    /** {@inheritDoc} */
    public void setDescription(String description) {
        if (equals(this.description, description)) {
            return;
        }
        String oldDesc = this.description;
        this.description = description;
        revalidate();
        getPropertyChangeSupport().firePropertyChange("description", oldDesc, description);
    }

    /** {@inheritDoc} */
    public boolean isMandatory() {
        return mandatory;
    }

    /** {@inheritDoc} */
    public void setMandatory(boolean mandatory) {
        if (equals(this.mandatory, mandatory)) {
            return;
        }
        boolean old = this.mandatory;
        this.mandatory = mandatory;
        revalidate();
        getPropertyChangeSupport().firePropertyChange("mandatory", old, mandatory);
    }

    /** {@inheritDoc} */
    public String getTitle() {
        return title;
    }

    /** {@inheritDoc} */
    public void setTitle(String title) {
        if (equals(this.title, title)) {
            return;
        }
        String old = this.title;
        this.title = title;
        revalidate();
        getPropertyChangeSupport().firePropertyChange("title", old, this.title);
    }

    /** {@inheritDoc} */
    public boolean isEditable() {
        return editable;
    }

    /** {@inheritDoc} */
    public void setEditable(boolean editable) {
        if (equals(this.editable, editable)) {
            return;
        }
        boolean old = this.editable;
        this.editable = editable;
        revalidate();
        getPropertyChangeSupport().firePropertyChange("editable", old, this.editable);
    }

    @Override
    public void revalidate() {
        if (isEmpty() && isMandatory() == false) {
            setValidationState(null);
        } else {
            super.revalidate();
        }
    }

    /**
     * This rule evaluates to invalid if the PM's "mandatory" attribute is true
     * and the PM is empty.
     *
     * @author Michael Karneim
     * @see AbstractValuePM#isMandatory()
     * @see AbstractValuePM#isEmpty()
     */
    public class MandatoryValidationRule implements ValidationRule {
        /** {@inheritDoc} */
        public ValidationState validate() {
            if (isEmpty()) {
                if (isMandatory()) {
                    String message = resourceBundle.getString(KEY_MESSAGE_MANDATORY);
                    String desc = getDescription();
                    if (desc != null) {
                        message += ": " + desc;
                    }
                    return new ValidationState(message);
                }
            }
            return null;
        }
    }
}