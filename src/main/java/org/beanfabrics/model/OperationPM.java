/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.beanfabrics.util.ResourceBundleFactory;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * The Operation is a presentation model that maintains a list of
 * {@link ExecutionMethod}s that will be executed whenever the
 * {@link #execute()} method is invoked.
 * 
 * @author Michael Karneim
 */
public class OperationPM extends AbstractOperationPM {
    protected static final String KEY_MESSAGE_NO_EXECUTION_METHODS = "message.noExecutionMethods";
    private final ResourceBundle resourceBundle = ResourceBundleFactory.getBundle(OperationPM.class);

    private List<ExecutionMethod> executionMethods = new ArrayList<ExecutionMethod>(0);

    public OperationPM() {
        getValidator().add(new DefaultValidationRule());
    }

    /** {@inheritDoc} */
    public void execute()
        throws Throwable {
        for (ExecutionMethod exe : executionMethods) {
            exe.execute();
        }
    }

    public synchronized Collection<ExecutionMethod> getExecutionMethods() {
        return Collections.unmodifiableCollection(executionMethods);
    }

    public synchronized void setExecutionMethods(Collection<ExecutionMethod> newExecutionMethods) {
        executionMethods = new ArrayList<ExecutionMethod>(newExecutionMethods);
        revalidate();
    }

    public synchronized void addExecutionMethod(ExecutionMethod exe) {
        List<ExecutionMethod> newExecutions = new ArrayList<ExecutionMethod>(executionMethods.size() + 1);
        newExecutions.addAll(executionMethods);
        newExecutions.add(exe);
        executionMethods = newExecutions;
        revalidate();
    }

    public synchronized void removeExecutionMethod(ExecutionMethod exe) {
        List<ExecutionMethod> newExecutionMethods = new ArrayList<ExecutionMethod>(executionMethods);
        newExecutionMethods.remove(exe);
        executionMethods = newExecutionMethods;
        revalidate();
    }

    public boolean containsExecutionMethod(ExecutionMethod exe) {
        return executionMethods.contains(exe);
    }

    public boolean hasExecutionMethods() {
        return executionMethods.isEmpty() == false;
    }

    public class DefaultValidationRule implements ValidationRule {
        public ValidationState validate() {
            if (hasExecutionMethods() == false) {
                String message = resourceBundle.getString(KEY_MESSAGE_NO_EXECUTION_METHODS);
                return new ValidationState(message);
            }
            return null;
        }
    }
}