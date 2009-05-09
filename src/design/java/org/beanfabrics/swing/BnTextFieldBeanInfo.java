/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.swing;

import java.awt.Image;
import java.beans.BeanInfo;

/**
 * @author Michael Karneim
 */
public class BnTextFieldBeanInfo extends ModelSubscriberBeanInfo {
	@SuppressWarnings("unchecked")
	@Override
	protected Class getBeanClass() {
		return BnTextField.class;
	}

	@Override
	protected boolean isPathBound() {
		return false;
	}

	@Override
	public Image getIcon(int iconKind) {
		switch (iconKind) {
		case BeanInfo.ICON_COLOR_16x16:
			return loadImage("bn_textfield_obj16.gif");
		default:
			return null;
		}
	}
}