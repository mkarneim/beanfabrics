/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import org.beanfabrics.swing.customizer.ModelSubscriberCustomizer;
import org.beanfabrics.util.ExceptionUtil;

/**
 * @author Michael Karneim
 */
public abstract class ModelSubscriberBeanInfo extends SimpleBeanInfo {
    @SuppressWarnings("unchecked")
    protected abstract Class getBeanClass();

    protected abstract boolean isPathBound();

    @Override
    public BeanDescriptor getBeanDescriptor() {
        final BeanDescriptor result = new BeanDescriptor(this.getBeanClass(), ModelSubscriberCustomizer.class);
        result.setValue("EXPLICIT_PROPERTY_CHANGE", Boolean.TRUE); // VE & WindowBuilder
        return result;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            final PropertyDescriptor provider = new PropertyDescriptor("modelProvider", this.getBeanClass(), "getModelProvider", "setModelProvider");
            final PropertyDescriptor path = new PropertyDescriptor("path", this.getBeanClass(), "getPath", "setPath");
            provider.setPreferred(true);
            provider.setShortDescription("set the ModelProvider");
            path.setPreferred(true);
            path.setBound(isPathBound());
            path.setShortDescription("set the path to the PresentationModel");
            return new PropertyDescriptor[] { provider, path };
        } catch (IntrospectionException ex) {
            ExceptionUtil.getInstance().handleException("getPropertyDescriptors", ex);
            return null;
        }
    }

    @Override
    public BeanInfo[] getAdditionalBeanInfo() {
        final Class superclass = this.getBeanClass().getSuperclass();
        try {
            final BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
            return new BeanInfo[] { superBeanInfo };
        } catch (IntrospectionException ex) {
            ExceptionUtil.getInstance().handleException("getAdditionalBeanInfo", ex);
            return null;
        }
    }
}