/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.table;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.beanfabrics.swing.ModelSubscriberBeanInfo;
import org.beanfabrics.swing.customizer.table.BnColumnPropertyEditor;
import org.beanfabrics.swing.customizer.table.BnTableCustomizer;
import org.beanfabrics.util.ExceptionUtil;

/**
 * @author Michael Karneim
 */
public class BnTableBeanInfo extends ModelSubscriberBeanInfo {
    private final static Class beanClass = BnTable.class;

    public BeanDescriptor getBeanDescriptor() {
        final BeanDescriptor result = new BeanDescriptor(beanClass, BnTableCustomizer.class);
        result.setValue("EXPLICIT_PROPERTY_CHANGE", Boolean.TRUE); // eclipse ide
        return result;
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            final PropertyDescriptor pModel = new PropertyDescriptor("presentationModel", this.getBeanClass(), "getPresentationModel", "setPresentationModel");

            final PropertyDescriptor provider = new PropertyDescriptor("modelProvider", beanClass, "getModelProvider", "setModelProvider");
            final PropertyDescriptor path = new PropertyDescriptor("path", beanClass, "getPath", "setPath");
            final PropertyDescriptor columns = new PropertyDescriptor("columns", beanClass, "getColumns", "setColumns");
            columns.setBound(true);
            columns.setPropertyEditorClass(BnColumnPropertyEditor.class);
            path.setBound(true);
            return new PropertyDescriptor[] { pModel, provider, path, columns };
        } catch (IntrospectionException ex) {
            ExceptionUtil.getInstance().handleException("", ex);
            return null;
        }
    }

    public java.awt.Image getIcon(int iconKind) {
        switch (iconKind) {
            case BeanInfo.ICON_COLOR_16x16:
                return loadImage("bn_table_obj16.gif");
            default:
                return null;
        }
    }

    public BeanInfo[] getAdditionalBeanInfo() {
        final Class superclass = beanClass.getSuperclass();
        try {
            final BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
            return new BeanInfo[] { superBeanInfo };
        } catch (IntrospectionException ex) {
            ExceptionUtil.getInstance().handleException("", ex);
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Class getBeanClass() {
        return beanClass;
    }

    @Override
    protected boolean isPathBound() {
        return false;
    }
}