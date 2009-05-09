/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.validation;


/**
 * The <code>Validatable</code> is an interface for classes that can be validated.
 *
 * @author Michael Karneim
 */
public interface Validatable {
	/**
	 * Updates the validation state of this object.
	 */
	public void revalidate();

	/**
	 * Returns the current validation state of this object.
	 *
	 * @return the current validation state of this object
	 */
	public ValidationState getValidationState();

	/**
	 * Returns <code>true</code> if this object is valid. This object is valid
	 * if it's validation state is <code>null</code>.
	 *
	 * @return <code>true</code> if this object is valid
	 */
	public boolean isValid();

	/**
	 * Returns the {@link Validator} of this validatable object.
	 *
	 * @return the <code>Validator</code> of this validatable object
	 */
	public Validator getValidator();
}