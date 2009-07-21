/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.util;

/**
 * The <code>IExceptionHandler</code> is the basic interface for an exception
 * handler that can be installed into the {@link ExceptionUtil}.
 * 
 * @author Michael Karneim
 */
public interface IExceptionHandler {
    /**
     * Handles the given Throwable.
     * 
     * @param message
     * @param t
     */
    public void handleException(String message, Throwable t);
}