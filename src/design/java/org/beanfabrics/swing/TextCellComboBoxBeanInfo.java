/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.swing;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import org.beanfabrics.swing.internal.TextPMComboBox;
import org.beanfabrics.util.ExceptionUtil;

/**
 * @author Michael Karneim
 */
public class TextCellComboBoxBeanInfo extends SimpleBeanInfo {
	private final static Class beanClass = TextPMComboBox.class;

	public PropertyDescriptor[] getPropertyDescriptors() {
		return new PropertyDescriptor[] {};
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
}