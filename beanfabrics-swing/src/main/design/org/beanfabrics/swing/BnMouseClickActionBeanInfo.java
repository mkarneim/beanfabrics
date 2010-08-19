/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import java.awt.Image;

/**
 * @author Michael Karneim
 */
public class BnMouseClickActionBeanInfo extends BnActionBeanInfo {
    @SuppressWarnings("unchecked")
    @Override
    protected Class getBeanClass() {
        return BnMouseClickAction.class;
    }

    @Override
    protected boolean isPathBound() {
        return false;
    }

    @Override
    public Image getIcon(int iconKind) {
        switch (iconKind) {
            // case BeanInfo.ICON_COLOR_16x16:
            // return loadImage("bn_action_obj16.gif");
            default:
                return null;
        }
    }
}