/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.log;

/**
 * A {@link Logger} that doesn't write any log entries.
 * 
 * @author Michael Karneim
 */
public class DummyLogger implements Logger {
    /** {@inheritDoc} */
    public void trace(Object msg, Throwable t) {
    }

    /** {@inheritDoc} */
    public void trace(Object msg) {
    }

    /** {@inheritDoc} */
    public void debug(Object msg, Throwable t) {
    }

    /** {@inheritDoc} */
    public void debug(Object msg) {
    }

    /** {@inheritDoc} */
    public void info(Object msg, Throwable t) {
    }

    /** {@inheritDoc} */
    public void info(Object msg) {
    }

    /** {@inheritDoc} */
    public void warn(Object msg, Throwable t) {
    }

    /** {@inheritDoc} */
    public void warn(Object msg) {
    }

    /** {@inheritDoc} */
    public void error(Object msg, Throwable t) {
    }

    /** {@inheritDoc} */
    public void error(Object msg) {
    }

    /** {@inheritDoc} */
    public void fatal(Object msg, Throwable t) {
    }

    /** {@inheritDoc} */
    public void fatal(Object msg) {
    }

    /** {@inheritDoc} */
    public boolean isTraceEnabled() {
        return false;
    }

    /** {@inheritDoc} */
    public boolean isDebugEnabled() {
        return false;
    }

    /** {@inheritDoc} */
    public boolean isInfoEnabled() {
        return false;
    }
}