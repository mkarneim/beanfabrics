package org.beanfabrics.samples.properties;

import org.beanfabrics.swing.ModelSubscriberBeanInfo;

/*
 * Created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
public class PropertiesPanelBeanInfo extends ModelSubscriberBeanInfo {
    @Override
    protected Class getBeanClass() {
        return PropertiesPanel.class;
    }

    @Override
    protected boolean isPathBound() {
        return false;
    }
}
