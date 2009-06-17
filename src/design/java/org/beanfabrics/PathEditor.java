/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;

import org.beanfabrics.util.ExceptionUtil;

/**
 * The <code>PathEditor</code> is a {@link PropertyEditor} for a {@link Path} object.
 * 
 * @author Michael Karneim
 */
public class PathEditor implements PropertyEditor {
	private static final String PATH = Path.class.getName();

	/** The path object that has to be set or changed. */
	private Path path;

	/** A supporter to handle the changes of properties. */
	private PropertyChangeSupport support = new PropertyChangeSupport(this);

	/**
	 * Set (or change) the object that is to be edited.
	 *
	 * @param value The object to be edited.
	 */
	public void setValue(Object value) {
		final Path old = this.getPath();
		Path newValue = (Path) value;
		this.path = newValue;
		this.support.firePropertyChange(null,null,null);
	}

	/**
	 * If the property value must be one of a set of known tagged values, then
	 * this method should return an array of the tags. 
	 *
	 * @return An array with the tagged values.
	 */
	public String[] getTags() {
		return null;
	}

	/**
	 * Gets the property value.
	 *
	 * @return The property value.
	 */
	public Object getValue() {
		return this.path;
	}

	/**
	 * A {@link PropertyEditor} may choose to make available a full custom {@link Component}
	 * that edits its property value.
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
	 * estate. 
	 *
	 * @param gfx
	 *            The graphics object to be painted.
	 * @param box
	 *            the area to be painted to
	 */
	public void paintValue(Graphics gfx, Rectangle box) {
	}

	/**
	 * Determines whether this property model supports a custom editor.
	 *
	 * @retun <code>true</code> if this property model supports a custom editor, otherwise
	 *        <code>false</code>
	 */
	public boolean supportsCustomEditor() {
		return false;
	}

	/**
	 * Register a listener for the PropertyChange event.
	 *
	 * @param listener
	 *            the listener to be added
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	/**
	 * Remove a listener for the PropertyChange event. 
	 *
	 * @param listener
	 *            The listener to be removed.
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
	 *     new Path("this.person.address.street")
	 * </pre>
	 *
	 */
	public String getJavaInitializationString() {
		try {
			if ( this.path == null) return null;
			String pathString = Path.getPathString( this.path);
			StringBuilder sb = new StringBuilder();
			sb.append("new ").append(PATH).append("(\"");
			sb.append( pathString);
			sb.append("\")");
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
			String pathString = Path.getPathString( this.path);
			if (pathString == null) return "";
			return pathString;
		} catch (Exception ex) {
			ExceptionUtil.getInstance().handleException("Can't call getAsText ", ex);
			return "";
		}
	}

	/**
	 * Set the property value by parsing a given String.
	 *
	 * @param text The new value for the property.
	 */
	public void setAsText(String text) {
		try {
			this.setValue(Path.parse(text));
		} catch (Exception e) {
			ExceptionUtil.getInstance().handleException("Can't call setAsText with param '" + text + "'", e);
		}
	}

	private Path getPath() {
		return this.path;
	}
}