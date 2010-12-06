/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * The {@link IconTextPM} is an extended {@link TextPM} that holds an additional
 * {@link Icon} attribute.
 * <p>
 * <b>Please Note:</b> this class will be moved to the
 * org.beanfabrics.swing.model package soon.
 * 
 * @author Michael Karneim
 */
public class IconTextPM extends TextPM implements IIconPM {
    private Icon icon;

    /**
     * Sets the {@link Icon} value of this PM.
     * 
     * @param newIcon
     */
    public void setIcon(Icon newIcon) {
        if (equals(newIcon, icon)) {
            return;
        }
        Icon old = this.icon;
        this.icon = newIcon;
        this.getPropertyChangeSupport().firePropertyChange("icon", old, newIcon);
    }

    /** {@inheritDoc} */
    public Icon getIcon() {
        return icon;
    }

    /**
     * Sets the {@link URL} to the resource containing an {@link Image} that
     * should be used as {@link Icon} value of this PM.
     * 
     * @param url the URL to the {@link Image} resource
     * @see #setIcon(Icon)
     */
    public void setIconUrl(URL url) {
        if (url == null) {
            this.setIcon(null);
        } else {
            Image img = Toolkit.getDefaultToolkit().getImage(url);
            ImageIcon imgIcon = new ImageIcon(img);
            this.setIcon(imgIcon);
        }
    }

}