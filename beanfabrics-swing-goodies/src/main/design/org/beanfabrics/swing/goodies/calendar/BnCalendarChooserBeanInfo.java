/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.goodies.calendar;

import java.beans.BeanInfo;

import org.beanfabrics.swing.ModelSubscriberBeanInfo;

/**
 * @author Michael Karneim
 */
public class BnCalendarChooserBeanInfo extends ModelSubscriberBeanInfo {
    @Override
    protected Class getBeanClass() {
        return BnCalendarChooser.class;
    }

    @Override
    protected boolean isPathBound() {
        return false;
    }

    @Override
    public java.awt.Image getIcon(int iconKind) {
        switch (iconKind) {
            case BeanInfo.ICON_COLOR_16x16:
                // TODO create an icon
                return loadImage("bn_calendarchooser_obj16.png");
            default:
                return null;
        }
    }
}
