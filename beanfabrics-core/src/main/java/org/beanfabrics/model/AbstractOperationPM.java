/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author Michael Karneim
 */
public abstract class AbstractOperationPM extends AbstractPM implements IOperationPM {
    private Icon icon;
    private String description = null;
    private String title;

    /**
     * Constructs a new {@link AbstractOperationPM}.
     */
    protected AbstractOperationPM() {
    }

    /** {@inheritDoc} */
    // TODO (mk) TEST
    public void setIcon(Icon newIcon) {
        if (equals(newIcon, icon)) {
            return;
        }
        Icon old = this.icon;
        this.icon = newIcon;
        this.getPropertyChangeSupport().firePropertyChange("icon", old, newIcon);
    }

    /** {@inheritDoc} */
    // TODO (mk) TEST
    public Icon getIcon() {
        return icon;
    }

    /** {@inheritDoc} */
    // TODO (mk) TEST
    public void setIconUrl(URL url) {
        Image img = Toolkit.getDefaultToolkit().getImage(url);
        ImageIcon imgIcon = new ImageIcon(img);
        this.setIcon(imgIcon);
    }

    /** {@inheritDoc} */
    public String getTitle() {
        return title;
    }

    /** {@inheritDoc} */
    public void setTitle(String title) {
        if (equals(this.title, title)) {
            return;
        }
        String old = this.title;
        this.title = title;
        this.revalidate();
        this.getPropertyChangeSupport().firePropertyChange("title", old, title);
    }

    /** {@inheritDoc} */
    public String getDescription() {
        return description;
    }

    /** {@inheritDoc} */
    public void setDescription(String description) {
        if (equals(this.description, description)) {
            return;
        }
        String oldDesc = this.description;
        this.description = description;
        this.revalidate();
        this.getPropertyChangeSupport().firePropertyChange("description", oldDesc, description);
    }

    /** {@inheritDoc} */
    public abstract boolean execute()
        throws Throwable;

    /** {@inheritDoc} */
    public boolean isEnabled() {
        return this.isValid();
    }

    /** {@inheritDoc} */
    public void check()
        throws IllegalStateException {
        if (!isEnabled()) {
            throw new IllegalStateException("Operation not enabled");
        }
    }
}