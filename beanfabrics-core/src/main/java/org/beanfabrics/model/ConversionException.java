/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

/**
 * The {@link ConversionException} is thrown to indicate that a
 * {@link PresentationModel} has attempted to convert a string value to another
 * type, but that the string does not have the appropriate format.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ConversionException extends RuntimeException {
    /**
     * Constructs a {@link ConversionException}.
     */
    public ConversionException() {
    }

    /**
     * Constructs a {@link ConversionException} with the given message.
     * 
     * @param message
     */
    public ConversionException(String message) {
        super(message);
    }

    /**
     * Constructs a {@link ConversionException} with the given cause.
     * 
     * @param cause
     */
    public ConversionException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a {@link ConversionException} with the given message and
     * cause.
     * 
     * @param message
     * @param cause
     */
    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}