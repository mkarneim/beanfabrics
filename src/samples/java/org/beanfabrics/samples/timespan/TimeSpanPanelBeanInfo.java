package org.beanfabrics.samples.timespan;

import org.beanfabrics.swing.ModelSubscriberBeanInfo;

/*
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
public class TimeSpanPanelBeanInfo extends ModelSubscriberBeanInfo {
    @Override
    protected Class getBeanClass() {
        return TimeSpanPanel.class;
    }

    @Override
    protected boolean isPathBound() {
        return false;
    }
}
