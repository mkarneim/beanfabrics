/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

/**
 * The {@link ExecutionMethod} is an interface for classes that can be used as a
 * implementation strategy for {@link OperationPM} objects. When
 * {@link OperationPM#execute()} is called it delegates the call to all
 * registered {@link ExecutionMethod}s.
 * <p>
 * An instance created from this class can be registered with a
 * {@link IOperationPM} using the
 * {@link OperationPM#addExecutionMethod(ExecutionMethod)} method.
 * 
 * @author Michael Karneim
 */
public interface ExecutionMethod {
    /**
     * Performs some action. Usually this method is called from
     * {@link OperationPM#execute()}. It returns <code>true</code> if 
     * the action has been successfully performed. It returns <code>false</code> or
     * throws an exception if the actions has not been performed successfully and
     * any subsequent operations should be skipped.
     * 
     * @returns <code>true</code> if the action was successfully performed
     * @throws Throwable if any exception occurs while performing the action
     */
    public boolean execute()
        throws Throwable;
}