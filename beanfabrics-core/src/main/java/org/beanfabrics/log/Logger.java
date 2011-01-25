/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.log;

/**
 * The {@link Logger} is the interface for several logging delegators available
 * for Beanfabrics.
 * 
 * @see <a
 *      href="http://www.beanfabrics.org/index.php/Logging_Example">Logging&nbsp;Example</a>
 * @author Max Gensthaler
 * @author Michael Karneim
 */
public interface Logger {
    /**
     * Returns if the TRACE level is enabled for this instance.
     * 
     * @return <code>true</code> if the TRACE level is enabled, else
     *         <code>false</code>
     */
    public boolean isTraceEnabled();

    /**
     * Logs a message at the TRACE level.
     * 
     * @param msg message to log
     */
    public void trace(String msg);

    /**
     * Returns if the DEBUG level is enabled for this instance.
     * 
     * @return <code>true</code> if the DEBUG level is enabled, else
     *         <code>false</code>
     */
    public boolean isDebugEnabled();

    /**
     * Logs a message at the DEBUG level.
     * 
     * @param msg message to log
     */
    public void debug(String msg);

    /**
     * Returns if the INFO level is enabled for this instance.
     * 
     * @return <code>true</code> if the INFO level is enabled, else
     *         <code>false</code>
     */
    public boolean isInfoEnabled();

    /**
     * Logs a message at the INFO level.
     * 
     * @param msg message to log
     */
    public void info(String msg);

    /**
     * Returns if the WARN level is enabled for this instance.
     * 
     * @return <code>true</code> if the WARN level is enabled, else
     *         <code>false</code>
     */
    public boolean isWarnEnabled();

    /**
     * Logs a message at the WARN level.
     * 
     * @param msg message to log
     */
    public void warn(String msg);

    /**
     * Logs an {@link Throwable} at the WARN level
     * 
     * @param msg the message to log
     * @param t the <code>Throwable</code> to log
     */
    public void warn(String msg, Throwable t);

    /**
     * Returns if the ERROR level is enabled for this instance.
     * 
     * @return <code>true</code> if the ERROR level is enabled, else
     *         <code>false</code>
     */
    public boolean isErrorEnabled();

    /**
     * Logs a message at the ERROR level.
     * 
     * @param msg message to log
     */
    public void error(String msg);

    /**
     * Logs an {@link Throwable} at the ERROR level
     * 
     * @param msg the message to log
     * @param t the <code>Throwable</code> to log
     */
    public void error(String msg, Throwable t);
}
