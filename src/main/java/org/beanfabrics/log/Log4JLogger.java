/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.log;

/**
 * A {@link Logger} that delegates all logging to <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>.
 * 
 * @author Michael Karneim
 */
public class Log4JLogger implements Logger {
    private org.apache.log4j.Logger delegate;

    /**
     * Constructs a <code>Log4JLogger</code> for a given {@link Class}.
     * 
     * @param clazz the <code>Class</code> to log for
     */
    @SuppressWarnings("unchecked")
    public Log4JLogger(Class clazz) {
        delegate = org.apache.log4j.Logger.getLogger(clazz);
    }

    /** {@inheritDoc} */
    public void trace(Object msg, Throwable t) {
        delegate.trace(msg, t);
    }

    /** {@inheritDoc} */
    public void trace(Object msg) {
        System.out.println("Log4JLogger.trace()");
        delegate.trace(msg);
    }

    /** {@inheritDoc} */
    public void debug(Object msg, Throwable t) {
        delegate.debug(msg, t);
    }

    /** {@inheritDoc} */
    public void debug(Object msg) {
        delegate.debug(msg);
    }

    /** {@inheritDoc} */
    public void info(Object msg, Throwable t) {
        delegate.info(msg, t);
    }

    /** {@inheritDoc} */
    public void info(Object msg) {
        delegate.info(msg);
    }

    /** {@inheritDoc} */
    public void warn(Object msg, Throwable t) {
        delegate.warn(msg, t);
    }

    /** {@inheritDoc} */
    public void warn(Object msg) {
        delegate.warn(msg);
    }

    /** {@inheritDoc} */
    public void error(Object msg, Throwable t) {
        delegate.error(msg, t);
    }

    /** {@inheritDoc} */
    public void error(Object msg) {
        delegate.error(msg);
    }

    /** {@inheritDoc} */
    public void fatal(Object msg, Throwable t) {
        delegate.fatal(msg, t);
    }

    /** {@inheritDoc} */
    public void fatal(Object msg) {
        delegate.fatal(msg);
    }

    /** {@inheritDoc} */
    public boolean isTraceEnabled() {
        return delegate.isTraceEnabled();
    }

    /** {@inheritDoc} */
    public boolean isDebugEnabled() {
        return delegate.isDebugEnabled();
    }

    /** {@inheritDoc} */
    public boolean isInfoEnabled() {
        return delegate.isInfoEnabled();
    }
}