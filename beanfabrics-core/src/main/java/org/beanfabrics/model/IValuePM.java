/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

/**
 * The {@link IValuePM} is the general interface of PM components that contain
 * some 'value' and other additional attributes which are generally usefull for
 * user interface programming, like title, description, mandatory, and editable.
 * 
 * @author Michael Karneim
 */
public interface IValuePM extends PresentationModel {
    /**
     * Returns the description of this PM. It contains some words for the user
     * that describe the type of information that is represented by this PM.
     * Usually it's displayed to the user as some kind of a tooltip.
     * 
     * @return the description of this PM
     */
    public String getDescription();

    /**
     * Sets the description of this PM.
     * 
     * @param description
     * @see #getDescription()
     */
    public void setDescription(String description);

    /**
     * Returns whether this PM requires a non-empty value. This PM is marked as
     * invalid if it's empty.
     * 
     * @return whether this PM requires a non-empty value
     * @see #isEmpty()
     */
    public boolean isMandatory();

    /**
     * Sets whether this PM requires a non-empty value.
     * 
     * @param mandatory set to <code>true</code> if this PM requires a non-empty
     *            value
     * @see IValuePM#isMandatory()
     */
    public void setMandatory(boolean mandatory);

    /**
     * Returns the title of this PM. It contains some words for the user to
     * identify the attribute represented by this PM in the context of a
     * containment structure. Some view components may display this title as a
     * field label.
     * 
     * @return the title of this PM.
     */
    public String getTitle();

    /**
     * Sets the title of this PM.
     * 
     * @param title
     * @see #getTitle()
     */
    public void setTitle(String title);

    /**
     * Returns whether the value of this PM could be modified by the view. If
     * set to <code>false</code> the view must not change the value but the
     * programmer still can do it.
     * 
     * @return whether the value of this PM could be modified by the view
     */
    public boolean isEditable();

    /**
     * Sets the specified boolean to indicate whether or not this PM is
     * editable.
     * 
     * @param editable the boolean to be set
     * @see #isEditable()
     */
    public void setEditable(boolean editable);

    /**
     * Returns whether the value of this PM is interpreted as an empty value.
     * 
     * @return <code>true</code> if this PM contains an empty value
     */
    public boolean isEmpty();

}