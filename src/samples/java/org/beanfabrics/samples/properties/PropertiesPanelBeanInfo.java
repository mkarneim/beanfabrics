package org.beanfabrics.samples.properties;

import org.beanfabrics.swing.ModelSubscriberBeanInfo;

/*
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
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
