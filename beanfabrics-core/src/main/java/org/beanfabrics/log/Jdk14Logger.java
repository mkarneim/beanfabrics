/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.log;

import java.util.logging.Level;

/**
 * The {@link Jdk14Logger} is a {@link Logger} delegator that delegates logging
 * requests to the Java {@link java.util.logging.Logger}.
 * 
 * @author Max Gensthaler
 */
public class Jdk14Logger implements Logger {
    private final java.util.logging.Logger delegate;

    /**
     * Constructs a <code>Jdk14Logger</code> for a given {@link Class}.
     * 
     * @param clazz the <code>Class</code> to log for
     */
    public Jdk14Logger(Class clazz) {
        delegate = java.util.logging.Logger.getLogger(clazz.getName());
    }

    /** {@inheritDoc} */
    public boolean isTraceEnabled() {
        return delegate.isLoggable(Level.FINEST);
    }

    /** {@inheritDoc} */
    public void trace(String msg) {
        delegate.log(Level.FINEST, msg);
    }

    /** {@inheritDoc} */
    public boolean isDebugEnabled() {
        return delegate.isLoggable(Level.FINE);
    }

    /** {@inheritDoc} */
    public void debug(String msg) {
        delegate.log(Level.FINE, msg);
    }

    /** {@inheritDoc} */
    public boolean isInfoEnabled() {
        return delegate.isLoggable(Level.INFO);
    }

    /** {@inheritDoc} */
    public void info(String msg) {
        delegate.log(Level.INFO, msg);
    }

    /** {@inheritDoc} */
    public boolean isWarnEnabled() {
        return delegate.isLoggable(Level.WARNING);
    }

    /** {@inheritDoc} */
    public void warn(String msg) {
        delegate.log(Level.WARNING, msg);
    }

    /** {@inheritDoc} */
    public void warn(String msg, Throwable t) {
        delegate.log(Level.WARNING, msg, t);
    }

    /** {@inheritDoc} */
    public boolean isErrorEnabled() {
        return delegate.isLoggable(Level.SEVERE);
    }

    /** {@inheritDoc} */
    public void error(String msg) {
        delegate.log(Level.SEVERE, msg);
    }

    /** {@inheritDoc} */
    public void error(String msg, Throwable t) {
        delegate.log(Level.SEVERE, msg, t);
    }
}