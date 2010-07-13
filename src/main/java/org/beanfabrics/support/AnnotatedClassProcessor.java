/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.support;

import java.lang.annotation.Annotation;

import org.beanfabrics.model.PresentationModel;

/**
 * @author Michael Karneim
 */
public interface AnnotatedClassProcessor extends AnnotationProcessor {
    void process(PresentationModel object, Class cls, Annotation annotation);
}