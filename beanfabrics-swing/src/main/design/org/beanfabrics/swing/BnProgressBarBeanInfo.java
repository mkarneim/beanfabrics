/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import java.awt.Image;
import java.beans.BeanInfo;

/**
 * @author Michael Karneim
 */
public class BnProgressBarBeanInfo extends ModelSubscriberBeanInfo {
    @SuppressWarnings("unchecked")
    @Override
    protected Class getBeanClass() {
        return BnProgressBar.class;
    }

    @Override
    protected boolean isPathBound() {
        return false;
    }

    @Override
    public Image getIcon(int iconKind) {
        switch (iconKind) {
            case BeanInfo.ICON_COLOR_16x16:
                return loadImage("bn_progressbar_obj16.gif");
            default:
                return null;
        }
    }
}