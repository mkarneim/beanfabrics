/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics;

import java.beans.PropertyChangeListener;

import org.beanfabrics.event.BnPropertyChangeSupport;

/**
 * The {@link Bean} is the basic interface for classes that can be observed by
 * {@link PropertyChangeListener} objects.
 * 
 * @author Michael Karneim
 */
public interface Bean {
    /**
     * Adds the given <code>PropertyChangeListener</code> to this bean. The
     * listener is registered for all bound properties of this class.
     * 
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Removes the given <code>PropertyChangeListener</code> from this bean.
     * This method should be used to remove PropertyChangeListeners that were
     * previously registered for all bound properties of this class.
     * 
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Adds the given <code>PropertyChangeListener</code> to this bean for the
     * specified property.
     * 
     * @param propertyName the property name of the property to observe
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    /**
     * Removes the given <code>PropertyChangeListener</code> from this bean for
     * the specified property. This method should be used to remove a listener
     * that was previously registered for a specific bound property.
     * 
     * @param propertyName the property name of the observed property
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

    // TODO (mk) do we need that?
    public BnPropertyChangeSupport getPropertyChangeSupport();
}