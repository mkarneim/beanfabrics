/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * The IconPM is a presentation model for a Swing icon.
 * 
 * @author Michael Karneim
 */
// TODO (mk) write TEST
public class IconPM extends AbstractValuePM implements IIconPM {
    private Icon icon;
    private Comparable comparable = new IconEditorComparable();

    public void setIcon(Icon newIcon) {
        if (equals(newIcon, icon)) {
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
        if (url == null) {
            this.setIcon(null);
        } else {
            Image img = Toolkit.getDefaultToolkit().getImage(url);
            ImageIcon imgIcon = new ImageIcon(img);
            this.setIcon(imgIcon);
        }
    }

    public boolean isEmpty() {
        return icon == null;
    }

    public Comparable<?> getComparable() {
        return comparable;
    }

    /**
     * Hashcode based Comparable.
     */
    private class IconEditorComparable implements Comparable {
        public Icon getIcon() {
            return icon;
        }

        /** {@inheritDoc} */
        public int compareTo(Object o) {
            if (o.getClass() != this.getClass()) {
                throw new IllegalArgumentException("Can't compare to unexpected comparable: " + o.getClass());
            }
            Icon a = this.getIcon();
            int aVal = a == null ? 0 : a.hashCode();
            Icon b = ((IconEditorComparable)o).getIcon();
            int bVal = b == null ? 0 : b.hashCode();
            return aVal - bVal;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (o.getClass() != getClass()) {
                return false;
            }
            Icon myIcon = this.getIcon();
            Icon otherIcon = ((IconEditorComparable)o).getIcon();
            return (myIcon == otherIcon ? otherIcon == null : myIcon.equals(otherIcon));
        }
    }
}