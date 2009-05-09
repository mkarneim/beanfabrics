/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.model;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author Michael Karneim
 */
// TODO (mk) JUNIT TEST
public class IconTextPM extends TextPM implements IIconPM {
	private Icon icon;

	public void setIcon( Icon newIcon) {
		if ( equals( newIcon, icon)) {
			return;
		}
		Icon old = this.icon;
		this.icon = newIcon;
		this.getPropertyChangeSupport().firePropertyChange("icon", old, newIcon);
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIconUrl(URL url) {
		if ( url == null) {
			this.setIcon(null);
		} else {
			Image img = Toolkit.getDefaultToolkit().getImage(url);
			ImageIcon imgIcon = new ImageIcon(img);
			this.setIcon(imgIcon);
		}
	}

	@Override
	public boolean isEmpty() {
		return icon == null && super.isEmpty();
	}
}