/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.beanfabrics.validation.ValidationState;

/**
 * This annotation is processed by the {@link ValidationSupport} and marks a
 * method as a validation method. Such a method is used from a validation rule.
 * It must either declare the boolean type as return type or a
 * {@link ValidationState}. It must not declare any parameter types.
 * <p>
 * For a more precise description of the validation framework, please see <a
 * href ="http://www.beanfabrics.org/wiki/index.php/Validation_Tutorial">
 * Beanfabrics tutorial on the validation framework</a>
 * </p>
 * 
 * @see org.beanfabrics.support.SortOrder
 * @author Michael Karneim
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Processor(ValidationProcessor.class)
public @interface Validation {
    String[] path() default "this";

    String message() default "";

    boolean validWhen() default true;
}