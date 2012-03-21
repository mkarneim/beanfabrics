/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics;

import org.beanfabrics.swing.ModelSubscriberBeanInfo;

/**
 * @author Michael Karneim
 */
public class BnModelObserverBeanInfo extends ModelSubscriberBeanInfo {
    @SuppressWarnings("unchecked")
    @Override
    protected Class getBeanClass() {
        return BnModelObserver.class;
    }

    @Override
    protected boolean isPathBound() {
        return false;
    }

    @Override
    public java.awt.Image getIcon(int iconKind) {
        switch (iconKind) {
            //		case BeanInfo.ICON_COLOR_16x16:
            //			return loadImage("bn_prop_observ_obj16.gif");
            default:
                return null;
        }
    }
}