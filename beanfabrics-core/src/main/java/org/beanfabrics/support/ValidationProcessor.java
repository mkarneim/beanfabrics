/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.beanfabrics.log.Logger;
import org.beanfabrics.log.LoggerFactory;
import org.beanfabrics.model.PresentationModel;

/**
 * @author Michael Karneim
 */
public class ValidationProcessor implements AnnotatedMethodProcessor {
    private static Logger LOG = LoggerFactory.getLogger(ValidationProcessor.class);

    public void process(PresentationModel object, Method method, Annotation annotation) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Processing " + SupportUtil.format(method));
        }
        ValidationSupport.get(object).setup(method);
    }
}