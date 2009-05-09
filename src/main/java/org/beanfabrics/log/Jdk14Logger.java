/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.log;

import java.util.logging.Level;

/**
 * A {@link Logger} that delegates all logging to the Java
 * {@link java.util.logging.Logger}.
 *
 * @author Max Gensthaler
 */
public class Jdk14Logger implements Logger {
	private java.util.logging.Logger delegate;

	/**
	 * Constructs a <code>Jdk14Logger</code> for a given {@link Class}.
	 *
	 * @param clazz
	 *            the <code>Class</code> to log for
	 */
	public Jdk14Logger(Class clazz) {
		delegate = java.util.logging.Logger.getLogger(clazz.getName());
	}

	/** {@inheritDoc} */
	public void trace(Object msg, Throwable t) {
		delegate.log(Level.FINEST, String.valueOf(msg), t);
	}

	/** {@inheritDoc} */
	public void trace(Object msg) {
		delegate.log(Level.FINEST, String.valueOf(msg));
	}

	/** {@inheritDoc} */
	public void debug(Object msg, Throwable t) {
		delegate.log(Level.FINER, String.valueOf(msg), t);
	}

	/** {@inheritDoc} */
	public void debug(Object msg) {
		delegate.log(Level.FINER, String.valueOf(msg));
	}

	/** {@inheritDoc} */
	public void warn(Object msg, Throwable t) {
		delegate.log(Level.FINE, String.valueOf(msg), t);
	}

	/** {@inheritDoc} */
	public void warn(Object msg) {
		delegate.log(Level.FINE, String.valueOf(msg));
	}

	/** {@inheritDoc} */
	public void info(Object msg, Throwable t) {
		delegate.log(Level.INFO, String.valueOf(msg), t);
	}

	/** {@inheritDoc} */
	public void info(Object msg) {
		delegate.log(Level.INFO, String.valueOf(msg));
	}

	/** {@inheritDoc} */
	public void error(Object msg, Throwable t) {
		delegate.log(Level.WARNING, String.valueOf(msg), t);
	}

	/** {@inheritDoc} */
	public void error(Object msg) {
		delegate.log(Level.WARNING, String.valueOf(msg));
	}

	/** {@inheritDoc} */
	public void fatal(Object msg, Throwable t) {
		delegate.log(Level.SEVERE, String.valueOf(msg), t);
	}

	/** {@inheritDoc} */
	public void fatal(Object msg) {
		delegate.log(Level.SEVERE, String.valueOf(msg));
	}

	/** {@inheritDoc} */
	public boolean isTraceEnabled() {
		return delegate.isLoggable(Level.FINEST);
	}

	/** {@inheritDoc} */
	public boolean isDebugEnabled() {
		return delegate.isLoggable(Level.FINER);
	}

	/** {@inheritDoc} */
	public boolean isInfoEnabled() {
		return delegate.isLoggable(Level.INFO);
	}
}