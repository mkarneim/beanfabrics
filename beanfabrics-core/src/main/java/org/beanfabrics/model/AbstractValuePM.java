/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * The {@link AbstractValuePM} is the general superclass of PM components that
 * implement the {@link IValuePM} interface.
 * <p>
 * Implementors usually represent information units that can be presented in a
 * single field, have a title, have a description, can be mandatory, and can be
 * editable.
 * 
 * @author Michael Karneim
 */
public abstract class AbstractValuePM extends AbstractPM implements IValuePM {
    private boolean editable = true;
    private boolean mandatory = false;
    private String description = null;
    private String title = null;

    /**
     * Constructs a {@link AbstractValuePM}.
     */
    protected AbstractValuePM() {
        // Please note: to disable default validation rules just call getValidator().clear();
        this.getValidator().add(new MandatoryValidationRule());
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
        this.revalidate();
        this.getPropertyChangeSupport().firePropertyChange("description", oldDesc, description);
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
        this.revalidate();
        this.getPropertyChangeSupport().firePropertyChange("mandatory", old, mandatory);
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
        this.revalidate();
        this.getPropertyChangeSupport().firePropertyChange("title", old, this.title);
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
        this.revalidate();
        this.getPropertyChangeSupport().firePropertyChange("editable", old, this.editable);
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
                    String message = "This value is mandatory"; // TODO i18n
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