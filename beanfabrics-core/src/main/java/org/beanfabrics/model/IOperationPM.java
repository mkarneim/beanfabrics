/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import java.net.URL;

import javax.swing.Icon;

import org.beanfabrics.validation.Validator;

/**
 * The {@link IOperationPM} represents a {@link PresentationModel} for view
 * components that can invoke an operation, like buttons and menu items.
 * 
 * @author Michael Karneim
 */
public interface IOperationPM extends PresentationModel {
    /**
     * Executes this operation.
     * 
     * @throws Throwable
     */
    public void execute()
        throws Throwable;

    /**
     * Returns whether this operation is enabled. If an operation is enabled it
     * can be executed.
     * 
     * @return <code>true</code> if this operation can be executed, otherwise
     *         <code>false</code>.
     */
    public boolean isEnabled();

    /**
     * Checks whether this operation is enabled. If it is enabled, it just
     * returns, otherwise it throws an {@link IllegalStateException}.
     * 
     * @throws IllegalStateException if this operation is not enabled
     */
    public void check()
        throws IllegalStateException;

    /**
     * Returns the description of this operation. It describes the intent of
     * this operation for the user.
     * <p>
     * A view displays this text usually in the form of a tooltip.
     * 
     * @return the description of this operation
     */
    public String getDescription();

    /**
     * Sets the description of this operation.
     * 
     * @param description
     * @see #getDescription()
     */
    public void setDescription(String description);

    /** {@inheritDoc} */
    public Validator getValidator();

    /**
     * Returns the title of this operation. Some views might use this text as
     * the label for the GUI widget.
     * 
     * @return the title of this operation
     */
    public String getTitle();

    /**
     * Sets the title of this operation.
     * 
     * @param title
     * @see #getTitle()
     */
    public void setTitle(String title);

    // TODO (mk) move method to new interface "IHaveIcon"
    /**
     * Returns the Icon of this operation. Some views might display this icon on
     * a GUI widget.
     */
    public Icon getIcon();

    /**
     * Sets the icon of this operation.
     * 
     * @param icon
     */
    // TODO (mk) remove
    public void setIcon(Icon icon);

    /**
     * Sets the {@link URL} pointing to the icon of this operation.
     * 
     * @param url
     */
    public void setIconUrl(URL url);
}