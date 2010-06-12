/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.log;

/**
 * A {@link Logger} that delegates all logging to <a
 * href="http://www.slf4j.org">Simple Logging Facade for Java</a>.
 * <p>
 * To use the SLF4J remember to import the required libraries to <a
 * href="http://www.slf4j.org/manual.html#binding">bind with the intended
 * logging framework at deployment time</a>.
 *
 * @author Max Gensthaler
 */
public class Slf4jLogger implements Logger {
    private final org.slf4j.Logger delegate;

    /**
     * Constructs a <code>Log4JLogger</code> for a given {@link Class}.
     *
     * @param clazz the <code>Class</code> to log for
     */
    @SuppressWarnings("unchecked")
    public Slf4jLogger(Class clazz) {
        delegate = org.slf4j.LoggerFactory.getLogger(clazz);
    }

    /** {@inheritDoc} */
	public boolean isTraceEnabled() {
		return delegate.isTraceEnabled();
	}

	/** {@inheritDoc} */
	public void trace(String msg) {
		delegate.trace(msg);
	}

	/** {@inheritDoc} */
	public boolean isDebugEnabled() {
		return delegate.isDebugEnabled();
	}

	/** {@inheritDoc} */
	public void debug(String msg) {
		delegate.debug(msg);
	}

    /** {@inheritDoc} */
	public boolean isInfoEnabled() {
		return delegate.isInfoEnabled();
	}

	/** {@inheritDoc} */
	public void info(String msg) {
		delegate.info(msg);
	}

	/** {@inheritDoc} */
	public boolean isWarnEnabled() {
		return delegate.isWarnEnabled();
	}

	/** {@inheritDoc} */
	public void warn(String msg) {
		delegate.warn(msg);
	}

	/** {@inheritDoc} */
	public void warn(String msg, Throwable t) {
		delegate.warn(msg, t);
	}

	/** {@inheritDoc} */
	public boolean isErrorEnabled() {
		return delegate.isErrorEnabled();
	}

	/** {@inheritDoc} */
	public void error(String msg) {
		delegate.error(msg);
	}

    /** {@inheritDoc} */
	public void error(String msg, Throwable t) {
		delegate.error(msg, t);
	}
}