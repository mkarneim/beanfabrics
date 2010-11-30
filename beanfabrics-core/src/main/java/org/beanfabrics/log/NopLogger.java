/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.log;

/**
 * The {@link NopLogger} is a logging delegator the doesn't delegate any log
 * messages.
 * 
 * @author Michael Karneim
 */
public class NopLogger implements Logger {
    /** {@inheritDoc} */
    public boolean isTraceEnabled() {
        return false;
    }

    /** {@inheritDoc} */
    public void trace(String msg) {
    }

    /** {@inheritDoc} */
    public boolean isDebugEnabled() {
        return false;
    }

    /** {@inheritDoc} */
    public void debug(String msg) {
    }

    /** {@inheritDoc} */
    public boolean isInfoEnabled() {
        return false;
    }

    /** {@inheritDoc} */
    public void info(String msg) {
    }

    /** {@inheritDoc} */
    public boolean isWarnEnabled() {
        return false;
    }

    /** {@inheritDoc} */
    public void warn(String msg) {
    }

    /** {@inheritDoc} */
    public void warn(String msg, Throwable t) {
    }

    /** {@inheritDoc} */
    public boolean isErrorEnabled() {
        return false;
    }

    /** {@inheritDoc} */
    public void error(String msg) {
    }

    /** {@inheritDoc} */
    public void error(String msg, Throwable t) {
    }
}