/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.swing.customizer.list;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;

import org.beanfabrics.Path;
import org.beanfabrics.swing.list.CellConfig;
import org.beanfabrics.util.ExceptionUtil;

/**
 * The <code>CellConfigPropertyEditor</code> is the JavaBeans {@link PropertyEditor} for a {@link CellConfig}.
 * 
 * @author Michael Karneim
 */
public class CellConfigPropertyEditor implements PropertyEditor {
	private static final String CELL_CONFIG_CLASSNAME = CellConfig.class.getName();
	private static final String PATH_CLASSNAME = org.beanfabrics.Path.class.getName();

	private CellConfig value;

	/** A supporter to fire property change events. */
	private PropertyChangeSupport support = new PropertyChangeSupport(this);

	/**
	 * Set (or change) the object that is to be edited.
	 * 
	 * @param value
	 *            The object to be edited.
	 */
	public void setValue(Object aValue) {
		final CellConfig oldValue = this.value;
		CellConfig newValue = (CellConfig) aValue;

		this.value = newValue;
		this.support.firePropertyChange("cellConfig", oldValue, newValue);
	}

	/**
	 * If the property value must be one of a set of known tagged values, then
	 * this method should return an array of the tags.�
	 * 
	 * @return An array with the tagged values.
	 */
	public String[] getTags() {
		return null;
	}

	/**
	 * Gets the property value.
	 * 
	 * @return the property value
	 */
	public Object getValue() {
		return this.value;
	}

	/**
	 * A {@link PropertyEditor} may choose to make available a full custom
	 * {@link Component} that edits its property value.
	 * 
	 * @return the custom editor component
	 */
	public Component getCustomEditor() {
		return null;
	}

	/**
	 * Determines whether this property model is paintable.
	 * 
	 * @return <code>true</code> is this property model is paintable, otherwise
	 *         <code>false</code>
	 */
	public boolean isPaintable() {
		return false;
	}

	/**
	 * Paint a representation of the value into a given area of screen real
	 * estate.�
	 * 
	 * @param gfx
	 *            The graphics object to be painted.
	 * @param box
	 *            The area to be painted to.
	 */
	public void paintValue(Graphics gfx, Rectangle box) {
	}

	/**
	 * Determines whether this property model supports a custom editor.
	 * 
	 * @retun <code>true</code> if this property model supports a custom editor,
	 *        otherwise <code>false</code>
	 */
	public boolean supportsCustomEditor() {
		return false;
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
	 * Remove a listener for the {@link PropertyChangeEvent}.�
	 * 
	 * @param listener
	 *            the listener to be removed
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	/**
	 * This method is intended for use when generating Java code to set the
	 * value of the property.
	 * 
	 * return The generated Java code, like:
	 * 
	 * <pre>
	 * new CellConfig(new Path(&quot;some.path&quot;));
	 * </pre>
	 * 
	 */
	public String getJavaInitializationString() {
		try {
			if (this.value == null) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			sb.append("new " + CELL_CONFIG_CLASSNAME + "(");
			sb.append(" new " + PATH_CLASSNAME + "(\"").append(this.value.getPath().getPathString() + "\")");
			sb.append(" )");
			return sb.toString();
		} catch (Exception ex) {
			ExceptionUtil.getInstance().handleException("Can't call getJavaInitializationString ", ex);
			return null;
		}
	}

	/**
	 * Gets the property value as text.
	 * 
	 * @return The property value as text.
	 */
	public String getAsText() {
		try {
			if (this.value == null) {
				return "";
			}
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			sb.append(Path.getPathString(this.value.getPath()));
			sb.append("]");
			return sb.toString();
		} catch (Exception ex) {
			ExceptionUtil.getInstance().handleException("Can't call getAsText ", ex);
			return null;
		}
	}

	/**
	 * Set the property value by parsing a given String.
	 * 
	 * @param text
	 *            The new value for the property.
	 */
	public void setAsText(String text) {
		try {
			final CellConfig oldValue = this.value;
			final CellConfig newValue;
			if (text == null || text.trim().length() == 0) {
				newValue = null;
			} else {
				int braceStart = text.indexOf("[");
				int braceEnd = text.indexOf("]");
				newValue = scan(text.substring(braceStart + 1, braceEnd));
			}
			this.support.firePropertyChange("columns", oldValue, newValue);

		} catch (Exception e) {
			ExceptionUtil.getInstance().handleException("Can't call setAsText with param '" + text + "'", e);
		}
	}

	private CellConfig scan(String text) throws IllegalArgumentException {
		if (text == null || text.trim().length() == 0) {
			return null;
		} else {
			return new CellConfig(new Path(text));
		}
	}
}