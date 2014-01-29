package org.beanfabrics.swing.customizer;

import org.beanfabrics.swing.ModelSubscriberBeanInfo;

/**
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
public class ModelSubscriberCustomizerPanelBeanInfo extends ModelSubscriberBeanInfo {
    @Override
    protected Class<?> getBeanClass() {
        return ModelSubscriberCustomizerPanel.class;
    }

    @Override
    protected boolean isPathBound() {
        return false;
    }
}
