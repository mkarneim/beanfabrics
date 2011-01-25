/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.beanfabrics.model.PresentationModel;

/**
 * @author Michael Karneim
 */
public interface AnnotatedMethodProcessor extends AnnotationProcessor {
    void process(PresentationModel object, Method method, Annotation annotation);
}