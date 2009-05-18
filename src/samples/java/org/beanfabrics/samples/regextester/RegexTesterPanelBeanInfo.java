package org.beanfabrics.samples.regextester;

import org.beanfabrics.swing.ModelSubscriberBeanInfo;

/**
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
public class RegexTesterPanelBeanInfo extends ModelSubscriberBeanInfo {
	@Override
	protected Class getBeanClass() {
		return RegexTesterPanel.class;
	}

	@Override
	protected boolean isPathBound() {
		return false;
	}
}

