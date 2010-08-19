/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * @author Michael Karneim
 */
public abstract class AbstractValuePM extends AbstractPM implements IValuePM {
    private boolean editable = true;
    private boolean mandatory = false;
    private String description = null;
    private String title = null;

    public AbstractValuePM() {
        this.getValidator().add(new DefaultValidationRule());
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

    public class DefaultValidationRule implements ValidationRule {
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