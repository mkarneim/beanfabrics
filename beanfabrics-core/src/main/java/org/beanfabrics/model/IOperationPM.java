/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.net.URL;

import javax.swing.Icon;

import org.beanfabrics.validation.Validator;

/**
 * @author Michael Karneim
 */
public interface IOperationPM extends PresentationModel {
    public void execute()
        throws Throwable;

    public boolean isEnabled();

    public void check()
        throws IllegalStateException;

    public String getDescription();

    public void setDescription(String description);

    /** {@inheritDoc} */
    public Validator getValidator();

    public String getTitle();

    public void setTitle(String title);

    // TODO (mk) move method to new interface "IHaveIcon"
    public Icon getIcon();

    // TODO (mk) remove
    public void setIcon(Icon icon);

    public void setIconUrl(URL url);
}