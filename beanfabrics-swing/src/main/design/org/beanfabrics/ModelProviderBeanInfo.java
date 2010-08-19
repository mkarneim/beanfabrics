/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics;

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
public class ModelProviderBeanInfo extends SimpleBeanInfo {
    private static Class BEAN_CLASS = ModelProvider.class;

    @Override
    public BeanDescriptor getBeanDescriptor() {
        final BeanDescriptor result = new BeanDescriptor(BEAN_CLASS, ModelSubscriberCustomizer.class);
        result.setValue("EXPLICIT_PROPERTY_CHANGE", Boolean.TRUE); // VE, WindowBuilder
        return result;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {

            final PropertyDescriptor pModelType = new PropertyDescriptor("pModelType", BEAN_CLASS, "getPresentationModelType", "setPresentationModelType");
            final PropertyDescriptor pModel = new PropertyDescriptor("pModel", BEAN_CLASS, "getPresentationModel", "setPresentationModel");

            return new PropertyDescriptor[] { pModelType, pModel };
        } catch (IntrospectionException ex) {
            ExceptionUtil.getInstance().handleException("getPropertyDescriptors", ex);
            return null;
        }
    }

    @Override
    public java.awt.Image getIcon(int iconKind) {
        switch (iconKind) {

            case BeanInfo.ICON_COLOR_16x16:
            default:
                return loadImage("modelprovider16x16.gif");
        }
    }

    @Override
    public BeanInfo[] getAdditionalBeanInfo() {
        final Class superclass = BEAN_CLASS.getSuperclass();
        try {
            final BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
            return new BeanInfo[] { superBeanInfo };
        } catch (IntrospectionException ex) {
            ExceptionUtil.getInstance().handleException("getAdditionalBeanInfo", ex);
            return null;
        }
    }
}