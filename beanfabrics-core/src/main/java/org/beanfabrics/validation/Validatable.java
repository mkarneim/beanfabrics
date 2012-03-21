/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.validation;

/**
 * The <code>Validatable</code> interface declares the interface for classes
 * that can be validated.
 * <p>
 * See the <a
 * href="http://www.beanfabrics.org/index.php/Validation">online&nbsp;
 * documentation on validation</a> for more information.
 * 
 * @author Michael Karneim
 */
public interface Validatable {
    /**
     * Updates the validation state of this object, usually by calling
     * {@link Validator#validate()}.
     */
    public void revalidate();

    /**
     * Returns the current validation state of this object.
     * 
     * @return the current validation state of this object
     */
    public ValidationState getValidationState();

    /**
     * Returns whether this object is valid. This object is valid if its
     * validation state is <code>null</code>.
     * 
     * @return <code>true</code> if this object is valid, otherwise
     *         <code>false</code>.
     */
    public boolean isValid();

    /**
     * Returns the {@link Validator} of this object. The validator is
     * responsible for creating the {@link ValidationState} of this object by
     * evaluating a specific set of {@link ValidationRule}s.
     * 
     * @return the <code>Validator</code> of this object
     */
    public Validator getValidator();
}