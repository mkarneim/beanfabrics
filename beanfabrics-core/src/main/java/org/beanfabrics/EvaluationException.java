/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics;

/**
 * An exception that indicates that something went wrong during the evaluation
 * of a {@link Path}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class EvaluationException extends Exception {
    /**
     * Creates a new EvaluationException.
     */
    public EvaluationException() {
        super();
    }

    /**
     * Creates a new EvaluationException.
     * 
     * @param message
     * @param cause
     */
    public EvaluationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new EvaluationException.
     * 
     * @param message
     */
    public EvaluationException(String message) {
        super(message);
    }

    /**
     * Creates a new EvaluationException.
     * 
     * @param cause
     */
    public EvaluationException(Throwable cause) {
        super(cause);
    }
}