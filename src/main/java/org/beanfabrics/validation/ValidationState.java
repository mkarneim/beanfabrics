/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.validation;

/**
 * The <code>ValidationState</code> is the result of a call to
 * {@link ValidationRule#validate()} and indicates that this rule has found some
 * invalid state. It contains a human readable text message which usually is
 * reported to the application user.
 *
 * @author Michael Karneim
 */
public class ValidationState {
	private final String message;

	/**
	 * Factory-method for creating a {@link ValidationState} from the specified
	 * message or <code>null</code> if the message is <code>null</code>.
	 *
	 * @param message
	 *            String
	 * @return the <code>ValidationState</code> if <code>message</code> is not
	 *         <code>null</code>, otherwise returns <code>null</code>
	 */
	public static ValidationState create(String message) {
		if (message == null) {
			return null;
		} else {
			return new ValidationState(message);
		}
	}

	/**
	 * Constructs a new instance with the given message.
	 *
	 * @param message
	 *            the message text of this <code>ValidationState</code>
	 * @throws IllegalArgumentException
	 *             when message is <code>null</code>
	 */
	public ValidationState(String message) throws IllegalArgumentException {
		if (message == null) {
			throw new IllegalArgumentException("message==null");
		}
		this.message = message;
	}

	/**
	 * Returns the message for this instance.
	 *
	 * @return String
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Returns <code>true</code> when this instance is equal to the given
	 * object.
	 *
	 * @param other
	 *            Object to compare <code>this</code> instance to
	 * @return boolean <code>true</code> if this instance and the given object
	 *         are equal, else <code>false</code>
	 */
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!other.getClass().equals(this.getClass())) {
			return false;
		}
		ValidationState otherState = (ValidationState) other;
		if (this.message == null && otherState.message == null) {
			return true;
		} else {
			return (this.message != null && this.message.equals(otherState.message));
		}
	}

    /** {@inheritDoc} */
    public int hashCode() {
    	int hashCode = 1;
    	hashCode = 31 * hashCode + (message == null ? 0 : message.hashCode());
    	return hashCode;
    }
}