/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;

import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.swing.table.BnColumnBuilder;
import org.beanfabrics.util.ExceptionUtil;

/**
 * The {@link BnColumnPropertyEditor} is a JavaBeans {@link PropertyEditor} for a {@link BnColumn}.
 * 
 * @author Michael Karneim
 */
public class BnColumnPropertyEditor implements PropertyEditor {

    /** The columns object array that has to be set or changed. */
    private BnColumn[] columns;

    /** A supporter to handle the changes of properties. */
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    private final ObjectToSourceCodeFormatter<BnColumn[]> javaFormat;

    public BnColumnPropertyEditor() {
        this( new BnColumnBuilderJavaFormat());
    }

    protected BnColumnPropertyEditor( ObjectToSourceCodeFormatter<BnColumn[]> javaFormat) {
        this.javaFormat = javaFormat;
    }

    /**
     * Register a listener for the {@link PropertyChangeEvent}.
     * 
     * @param listener
     *            the listener to be added
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Remove a listener for the {@link PropertyChangeEvent}.
     * 
     * @param listener
     *            the listener to be removed
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    /**
     * Set (or change) the object that is to be edited.
     * 
     * @param value
     *            The object to be edited.
     */
    public void setValue(Object value) {
        // final BnColumn[] old = this.getColumns();
        BnColumn[] newValue = (BnColumn[]) value;

        // TODO eclipse workaround, to be checked again
        if (newValue != null && newValue.length == 0) {
            newValue = null;
        }

        this.columns = newValue;
        this.support.firePropertyChange(null, null, null);
    }

    /**
     * Gets the property value.
     * 
     * @return the property value
     */
    public Object getValue() {
        return this.columns;
    }

    /**
     * This method is intended for use when generating Java code to set the value of the property.
     * 
     * @return The generated Java code, see {@link BnColumnBuilder}
     */
    public String getJavaInitializationString() {
        try {
            String result = javaFormat.format(this.columns);
            return result;
        } catch (Exception ex) {
            ExceptionUtil.getInstance().handleException("Can't generate java initialization string!", ex);
            return null;
        }
    }

    @Override
    public String getAsText() {
        return null;
    }

    @Override
    public void setAsText(String text) {
        throw new IllegalArgumentException("this kind of property can't be expressed as text!");
    }

    /**
     * Determines whether this property model supports a custom editor.
     * 
     * @retun <code>true</code> if this property model supports a custom editor, otherwise <code>false</code>
     */
    public boolean supportsCustomEditor() {
        return false;
    }

    /**
     * A {@link PropertyEditor} may choose to make available a full custom {@link Component} that edits its property
     * value.
     * 
     * @return the custom editor component
     */
    public Component getCustomEditor() {
        return null;
    }

    /**
     * Determines whether this property model is paintable.
     * 
     * @return <code>true</code> is this property model is paintable, otherwise <code>false</code>
     */
    public boolean isPaintable() {
        return false;
    }

    /**
     * Paint a representation of the value into a given area of screen real estate.
     * 
     * @param gfx
     *            The graphics object to be painted.
     * @param box
     *            The area to be painted to.
     */
    public void paintValue(Graphics gfx, Rectangle box) {
    }

    /**
     * If the property value must be one of a set of known tagged values, then this method should return an array of the
     * tags.
     * 
     * @return An array with the tagged values.
     */
    public String[] getTags() {
        return null;
    }

}