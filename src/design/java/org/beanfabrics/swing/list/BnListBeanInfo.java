/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.swing.list;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.beanfabrics.swing.ModelSubscriberBeanInfo;
import org.beanfabrics.swing.customizer.list.BnListCustomizer;
import org.beanfabrics.swing.customizer.list.CellConfigPropertyEditor;
import org.beanfabrics.util.ExceptionUtil;

/**
 * @author Max Gensthaler
 * @author Michael Karneim
 */
public class BnListBeanInfo extends ModelSubscriberBeanInfo {
	private static final Class BEAN_CLASS = BnList.class;

	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor result = new BeanDescriptor(BEAN_CLASS, BnListCustomizer.class);
		// for eclipse ide
		result.setValue("EXPLICIT_PROPERTY_CHANGE", Boolean.TRUE);
		return result;
	}

	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			final PropertyDescriptor provider = new PropertyDescriptor("modelProvider",
					BEAN_CLASS, "getModelProvider", "setModelProvider");
			PropertyDescriptor path = new PropertyDescriptor("path", BEAN_CLASS, "getPath", "setPath");
			path.setBound(true);			
			final PropertyDescriptor cellConfig = new PropertyDescriptor(
					"cellConfig", BEAN_CLASS, "getCellConfig", "setCellConfig");
			cellConfig.setBound(true);
			cellConfig.setPropertyEditorClass(CellConfigPropertyEditor.class);
			return new PropertyDescriptor[]{provider, path};
		} catch (IntrospectionException ex) {
			ExceptionUtil.getInstance().handleException("", ex);
			return null;
		}
	}

	public Image getIcon(int iconKind) {
		switch (iconKind) {
			case BeanInfo.ICON_COLOR_16x16 :
				return loadImage("bn_list_obj16.gif");
			default :
				return null;
		}
	}

	public BeanInfo[] getAdditionalBeanInfo() {
		final Class superclass = BEAN_CLASS.getSuperclass();
		try {
			BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
			return new BeanInfo[]{superBeanInfo};
		} catch (IntrospectionException ex) {
			ExceptionUtil.getInstance().handleException("", ex);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class getBeanClass() {
		return BEAN_CLASS;
	}

	@Override
	protected boolean isPathBound() {
		return false;
	}
}