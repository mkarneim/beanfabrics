/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import javax.swing.Icon;

/**
 * The {@link IconPM} is an interface for {@link PresentationModel} classes that
 * contain a Swing {@link Icon}.
 * <p>
 * <b>Please Note:</b> this class will be moved to the
 * org.beanfabrics.swing.model package soon.
 * 
 * @author Michael Karneim
 */
public interface IIconPM extends IValuePM {
    /**
     * Returns the {@link Icon} value of this PM.
     * 
     * @return the {@link Icon} value of this PM
     */
    public Icon getIcon();
}