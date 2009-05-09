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

import org.beanfabrics.swing.internal.TextPMTextField;
import org.beanfabrics.util.ExceptionUtil;

/**
 * @author Michael Karneim
 */
public class TextCellTextFieldBeanInfo extends SimpleBeanInfo {
	private final static Class beanClass = TextPMTextField.class;

	// TODO (mk) if we return null for getPropertyDescriptors we can't customize
	// any subclass of bean
	// TODO (mk) if we return new PropertyInfo[] { }; we can't see the
	// selectAllOnFocusGainedEnabled in the properties inspector

	public PropertyDescriptor[] getPropertyDescriptors() {
		return new PropertyDescriptor[] {};
		// try {
		// final PropertyDescriptor selectAllOnFocusGainedEnabled = new
		// PropertyDescriptor("selectAllOnFocusGainedEnabled",
		// beanClass, "isSelectAllOnFocusGainedEnabled",
		// "setSelectAllOnFocusGainedEnabled");
		// return new PropertyDescriptor[] { selectAllOnFocusGainedEnabled};
		// } catch (IntrospectionException ex) {
		// ExceptionUtil.handleException("", ex);
		// return new PropertyDescriptor[] { };
		// }
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