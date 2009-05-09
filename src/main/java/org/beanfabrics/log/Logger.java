/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.log;

/**
 * @author Michael Karneim
 */
public interface Logger {
	/**
	 * Helps to trace the application, i.e. by writing to the logger at the
	 * beginning of the method:
	 * <pre>
	 * if (LOG.isTraceEnabled()) {
	 *     LOG.trace("enter Logger.trace(Object,Throwable)"
	 * }
	 * </pre>
	 * @param msg message to log
	 * @param t <code>Throwable</code> to log
	 * @see #isTraceEnabled()
	 */
	public void trace(Object msg, Throwable t);

	/**
	 * Helps to trace the application, i.e. by writing to the logger at the
	 * beginning of the method:
	 * <pre>
	 * if (LOG.isTraceEnabled()) {
	 *     LOG.trace(&quot;enter Logger.trace(Object)&quot;
	 * }
	 * </pre>
	 * @param msg message to log
	 * @see #isTraceEnabled()
	 */
	public void trace(Object msg);

	/**
	 * Writes debug messages which under normal circumstances (if debug mode
	 * is not enabled) should not be shown.
	 * @param msg message to log
	 * @param t <code>Throwable</code> to log
	 * @see #isDebugEnabled()
	 */
	public void debug(Object msg, Throwable t);

	/**
	 * Writes debug messages which under normal circumstances (if debug mode
	 * is not enabled) should not be shown.
	 * @param msg message to log
	 * @see #isDebugEnabled()
	 */
	public void debug(Object msg);

	/**
	 * Writes information messages.
	 * @param msg message to log
	 * @param t <code>Throwable</code> to log
	 * @see #isDebugEnabled()
	 */
	public void info(Object msg, Throwable t);

	/**
	 * Writes information messages.
	 * @param msg message to log
	 * @see #isInfoEnabled()
	 */
	public void info(Object msg);

	/**
	 * Writes warning messages.
	 * @param msg message to log
	 * @param t <code>Throwable</code> to log
	 * @see #isInfoEnabled()
	 */
	public void warn(Object msg, Throwable t);

	/**
	 * Writes warning messages.
	 * @param msg message to log
	 */
	public void warn(Object msg);

	/**
	 * Writes error messages.
	 * @param msg message to log
	 * @param t <code>Throwable</code> to log
	 */
	public void error(Object msg, Throwable t);

	/**
	 * Writes error messages.
	 * @param msg message to log
	 */
	public void error(Object msg);

	/**
	 * Writes fatal error messages.
	 * @param msg message to log
	 * @param t <code>Throwable</code> to log
	 */
	public void fatal(Object msg, Throwable t);

	/**
	 * Writes fatal error messages.
	 * @param msg message to log
	 */
	public void fatal(Object msg);

	/**
	 * Returns if the trace level is enabled.
	 * Should be checked before any call of {@link #trace(Object)} or {@link #trace(Object,Throwable)}.
	 * @return <code>true</code> if the trace level is enabled, else <code>false</code>
	 */
	public boolean isTraceEnabled();

	/**
	 * Returns if the debug level is enabled.
	 * Should be checked before any call of {@link #debug(Object)} or {@link #debug(Object,Throwable)}.
	 * @return <code>true</code> if the debug level is enabled, else <code>false</code>
	 */
	public boolean isDebugEnabled();

	/**
	 * Returns if the info level is enabled.
	 * Should be checked before any call of {@link #info(Object)} or {@link #info(Object,Throwable)}.
	 * @return <code>true</code> if the info level is enabled, else <code>false</code>
	 */
	public boolean isInfoEnabled();
}