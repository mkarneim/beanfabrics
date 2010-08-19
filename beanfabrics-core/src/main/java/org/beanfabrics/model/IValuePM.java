/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

/**
 * @author Michael Karneim
 */
public interface IValuePM extends PresentationModel {
    public String getDescription();

    public void setDescription(String description);

    public boolean isMandatory();

    public void setMandatory(boolean mandatory);

    public String getTitle();

    public void setTitle(String title);

    public boolean isEditable();

    public void setEditable(boolean editable);

    /**
     * Return <code>true</code> if the content of this model is interpreted as
     * empty.
     * 
     * @return <code>true</code> if content is empty, else <code>false</code>
     */
    public boolean isEmpty();

    public Comparable getComparable();
}