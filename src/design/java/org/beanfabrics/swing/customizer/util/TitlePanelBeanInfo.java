/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.util;

import org.beanfabrics.swing.ModelSubscriberBeanInfo;

/**
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
public class TitlePanelBeanInfo extends ModelSubscriberBeanInfo {
    @Override
    protected Class getBeanClass() {
        return TitlePanel.class;
    }

    @Override
    protected boolean isPathBound() {
        return false;
    }
}