/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.goodies.calendar;

import java.beans.BeanInfo;

import org.beanfabrics.swing.ModelSubscriberBeanInfo;

/**
 * @author Max Gensthaler
 */
public class BnCalendarChooserButtonBeanInfo extends ModelSubscriberBeanInfo {
    @SuppressWarnings("unchecked")
    @Override
    protected Class getBeanClass() {
        return BnCalendarChooserButton.class;
    }

    @Override
    protected boolean isPathBound() {
        return false;
    }

    @Override
    public java.awt.Image getIcon(int iconKind) {
        switch (iconKind) {
            case BeanInfo.ICON_COLOR_16x16:
                return loadImage("bn_calendarchooser_obj16.png");
            default:
                return null;
        }
    }
}
