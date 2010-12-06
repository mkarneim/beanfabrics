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
 * The {@link IconPM} is a {@link PresentationModel} that contains a Swing
 * {@link Icon}.
 * <p>
 * <b>Please Note:</b> this class will be moved to the
 * org.beanfabrics.swing.model package soon.
 * 
 * @author Michael Karneim
 */
public class IconPM extends AbstractValuePM implements IIconPM {
    private Icon icon;
    private Comparable<?> comparable = new IconComparable();

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

    /** {@inheritDoc} */
    public boolean isEmpty() {
        return icon == null;
    }

    /** {@inheritDoc} */
    public Comparable<?> getComparable() {
        return comparable;
    }

    /**
     * The {@link IconComparable} delegates the comparison to the {@link Icon}
     * or, if the Icon is an {@link ImageIcon} to the icon's {@link Image}
     * property. Since both classes do not implement the {@link Comparable}
     * interface, this implementation uses the identity hashcodes for
     * comparison.
     */
    @SuppressWarnings("unchecked")
    public class IconComparable implements Comparable {
        /**
         * Returns the {@link Icon} used for comparison.
         * 
         * @return the Icon used for comparison
         */
        protected Icon getIcon() {
            return icon;
        }

        /** {@inheritDoc} */
        public int compareTo(Object o) {
            if (!(o instanceof IconComparable)) {
                throw new IllegalArgumentException("o must be instance of" + IconComparable.class);
            }
            IconComparable oc = (IconComparable)o;
            Icon thisIcon = getIcon();
            if (thisIcon == null) {
                if (oc.getIcon() == null) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                Icon oIcon = oc.getIcon();
                if (oIcon == null) {
                    return 1;
                } else {
                    if (thisIcon instanceof ImageIcon && oIcon instanceof ImageIcon) {
                        ImageIcon thisImgIcon = (ImageIcon)thisIcon;
                        ImageIcon oImgIcon = (ImageIcon)oIcon;
                        return System.identityHashCode(thisImgIcon.getImage()) - System.identityHashCode(oImgIcon.getImage());
                    } else {
                        return System.identityHashCode(thisIcon) - System.identityHashCode(oIcon);
                    }
                }
            }
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
            Icon thisIcon = this.getIcon();
            Icon oIcon = ((IconComparable)o).getIcon();

            if (thisIcon == null || oIcon == null) {
                return false;
            } else {
                if (thisIcon instanceof ImageIcon && oIcon instanceof ImageIcon) {
                    ImageIcon thisImgIcon = (ImageIcon)thisIcon;
                    ImageIcon oImgIcon = (ImageIcon)oIcon;
                    return thisImgIcon.getImage().equals(oImgIcon.getImage());
                } else {
                    return thisIcon.equals(oIcon);
                }
            }
        }
    }
}