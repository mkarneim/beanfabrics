/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EventObject;

/**
 * @author Michael Karneim
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Processor(OnChangeProcessor.class)
public @interface OnChange {
    String[] path() default "this";
    Class<? extends EventObject>[]event() default {};

}