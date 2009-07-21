/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.util;

/**
 * The <code>ExceptionUtil</code> is a singleton that provides access to an
 * {@link IExceptionHandler} which is used by the Beanfabrics framework to
 * handle exceptions.
 * 
 * @author Max Gensthaler
 */
public class ExceptionUtil {
    private static ExceptionUtil instance;

    private IExceptionHandler exceptionHandler;

    private ExceptionUtil() {
        //
    }

    public static ExceptionUtil getInstance() {
        if (instance == null) {
            instance = new ExceptionUtil();
        }
        return instance;
    }

    public void handleException(String message, Throwable t) {
        if (this.exceptionHandler != null) {
            this.exceptionHandler.handleException(message, t);
        } else {
            ErrorPane.showErrorDialog(message, t);
        }
    }

    public void setExceptionHandler(IExceptionHandler handler) {
        this.exceptionHandler = handler;
    }
}