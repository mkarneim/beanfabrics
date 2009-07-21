/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.validation;

/**
 * A <code>ValidationRule</code> is a rule that evaluates the current execution
 * context in respect of a certain set of constraints. It is responsible for
 * producing a {@link ValidationState} as the result whereas a <code>null</code>
 * result means 'valid' and a not-null result means 'invalid'. <pM> Usually
 * multiple rules are composed into a {@link Validator} which normally are used
 * by {@link Validatable} objects for validating their state.
 * 
 * @see <a
 *      href="http://www.beanfabrics.org/wiki/index.php/Validation_Tutorial">online
 *      tutorial on the validation framework< /a>
 * @author Michael Karneim
 */
public interface ValidationRule {
    /**
     * Produces a fresh {@link ValidationState}. A <code>null</code> return
     * value indicates that this rule has been successfully validated. A not-
     * <code>null</code> value indicates that this rule has found an invalid
     * state.
     * 
     * @return the fresh validation result of this rule.
     */
    public ValidationState validate();
}