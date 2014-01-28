package org.beanfabrics.swing.customizer.util;

import org.beanfabrics.swing.ModelSubscriberBeanInfo;

/**
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
public class SampleListPanelBeanInfo extends ModelSubscriberBeanInfo {
    @Override
    protected Class<?> getBeanClass() {
        return SampleListPanel.class;
    }

    @Override
    protected boolean isPathBound() {
        return false;
    }
}
