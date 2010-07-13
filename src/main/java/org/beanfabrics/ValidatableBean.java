/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.beanfabrics.validation.Validatable;
import org.beanfabrics.validation.ValidationState;
import org.beanfabrics.validation.Validator;

/**
 * This is an implementation of <code>Validatable</code> that uses a
 * <code>Validator</code> for producing a fresh <code>ValidationState</code>.
 * 
 * @author Michael Karneim
 */
public class ValidatableBean extends AbstractBean implements Validatable {
    private final Validator validator = new Validator();
    private ValidationState validationState = null;

    /**
     * Constructs a new instance.
     */
    public ValidatableBean() {
        this.validator.addPropertyChangeListener("rules", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                // whenever rules are changed, we have to revalidate
                revalidate();
            }
        });
    }

    /** {@inheritDoc} */
    public void revalidate() {
        this.setValidationState(this.validator.validate());
    }

    /** {@inheritDoc} */
    public final ValidationState getValidationState() {
        return validationState;
    }

    /** {@inheritDoc} */
    public final boolean isValid() {
        return this.getValidationState() == null;
    }

    /** {@inheritDoc} */
    public final Validator getValidator() {
        return validator;
    }

    /**
     * Replaces the validation state. This method should not be called directly
     * since it is called from the {@link #revalidate()} method.
     */
    protected final void setValidationState(ValidationState validationState) {
        if (this.validationState == validationState || (this.validationState != null && this.validationState.equals(validationState))) {
            return; // nothing has changed
        }
        ValidationState oldState = this.validationState;
        this.validationState = validationState;
        this.getPropertyChangeSupport().firePropertyChange("validationState", oldState, validationState);
    }
}