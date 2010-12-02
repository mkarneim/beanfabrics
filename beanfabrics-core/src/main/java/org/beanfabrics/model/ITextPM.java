/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

/**
 * The {@link ITextPM} is the interface of PM components that support a text
 * value.
 * 
 * @author Michael Karneim
 */
public interface ITextPM extends IValuePM {
    /**
     * Returns the text value of this PM.
     * 
     * @return the text value
     */
    public String getText();

    /**
     * Set the value of this OPm to the given text. If the argument is
     * <code>null</code>, it will be changed into the empty string
     * <code>""</code>.
     * 
     * @param aText the text value
     */
    public void setText(String aText);

    /**
     * Returns whether this PM is modified. This PM is modified if the text
     * value isn't equal to the default text.
     * 
     * @see #getText()
     * @see #preset()
     */
    public boolean isModified();

    /**
     * Sets the value of this PM to the value of the default text.
     */
    public void reset();

    /**
     * Sets the default text of this PM to the text value of this PM.
     */
    public void preset();

    /**
     * Sets the {@link Options}. This attribute is used by some view components
     * to show possible text values that can be choosen.
     * 
     * @param options
     */
    public void setOptions(Options options);

    /**
     * Returns the options.
     * 
     * @return the options
     * @see #setOptions(Options)
     */
    public Options getOptions();

    /**
     * Reformats the text value by first parsing it and the formatting it with
     * an appropriate format. It is not required that this method has any
     * effect.
     */
    public void reformat();
}