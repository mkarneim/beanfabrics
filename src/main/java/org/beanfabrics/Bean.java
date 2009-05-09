/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics;

import java.beans.PropertyChangeListener;

import org.beanfabrics.event.BnPropertyChangeSupport;

/**
 * Basic interface for classes that can be observed by {@link PropertyChangeListener} objects.
 * 
 * @author Michael Karneim
 */
public interface Bean {
	/**
	 * Adds a PropertyChangeListener to the listener list. 
	 * The listener is registered for all bound properties of this class.
	 * @param l the PropertyChangeListener to be added
	 */
	public void addPropertyChangeListener(PropertyChangeListener l);
	/**
	 * Removes a PropertyChangeListener from the listener list. 
	 * This method should be used to remove PropertyChangeListeners that were registered for all bound properties of this class.
	 * @param l the PropertyChangeListener to be removed
	 */
	public void removePropertyChangeListener(PropertyChangeListener l);
	/**
	 * Adds a PropertyChangeListener to the listener list for a specific property.
	 * @param propertyName a valid property name 
	 * @param l the PropertyChangeListener to be added
	 */
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener l);
	/**
	 * Removes a PropertyChangeListener from the listener list for a specific property. 
	 * This method should be used to remove PropertyChangeListeners that were registered for a specific bound property.
	 * @param propertyName a valid property name
	 * @param l the PropertyChangeListener to be removed
	 */
	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener l);

	// TODO (mk) do we need that?
	public BnPropertyChangeSupport getPropertyChangeSupport();
}