/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.beanfabrics.log.Logger;
import org.beanfabrics.log.LoggerFactory;
import org.beanfabrics.model.PresentationModel;

/**
 * @author Michael Karneim
 */
public class ServiceProcessor implements AnnotatedMethodProcessor, AnnotatedFieldProcessor {
    private static Logger LOG = LoggerFactory.getLogger(ServiceProcessor.class);

    public void process(PresentationModel object, Method method, Annotation annotation) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Processing " + SupportUtil.format(method));
        }
        ServiceSupport.get(object).setup(method);
    }

    public void process(PresentationModel object, Field field, Annotation annotation) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Processing " + SupportUtil.format(field));
        }
        ServiceSupport.get(object).setup(field);
    }
}