/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
/*
 * This class is slightly based on Sun's PropertyChangeSupport source which has
 * the following copyright notice: Copyright 2002 Sun Microsystems, Inc. All
 * rights reserved. SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license
 * terms.
 */
package org.beanfabrics.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * The {@link BnPropertyChangeSupport} is a utility class for handling listeners
 * of bound properties.
 * 
 * @author Michael Karneim
 */
public class BnPropertyChangeSupport {
    private final WeakHashMap<PropertyChangeListener, WeakWrapper> weakWrapper = new WeakHashMap<PropertyChangeListener, WeakWrapper>();
    private List<PropertyChangeListener> listeners;

    /**
     * {@link Map} for managing listeners on named properties. Maps each
     * property name to a {@link BnPropertyChangeSupport} object.
     */
    private Map<String, BnPropertyChangeSupport> children;

    /**
     * The object to be provided as the "source" for any generated events.
     */
    private Object source;

    /**
     * Constructs a {@link BnPropertyChangeSupport}.
     * 
     * @param sourceBean The bean to be given as the source for any events
     */
    public BnPropertyChangeSupport(Object sourceBean) {
        if (sourceBean == null) {
            throw new NullPointerException();
        }
        source = sourceBean;
    }

    /**
     * Adds the given {@link PropertyChangeListener} for any property of the
     * source bean.
     * 
     * @param listener the listener add
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if (listener instanceof WeakListener) {
            listener = this.getWeakWrapper(listener);
        }
        if (listeners == null) {
            listeners = new ArrayList<PropertyChangeListener>();
        } else {
            this.removeUnusedWeakWrappers(listeners);
        }
        listeners.add(listener);
    }

    /**
     * Removes the given {@link PropertyChangeListener}.
     * 
     * @param listener the listener to remove
     */
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        if (listeners == null) {
            return;
        }
        if (listener instanceof WeakListener) {
            listener = this.getWeakWrapper(listener);
            this.removeWeakWrapper((WeakWrapper)listener);
        }

        listeners.remove(listener);
    }

    /**
     * Adds the given {@link PropertyChangeListener} for the specified property
     * of the source bean. The listener will only receive events that are
     * triggered by that property.
     * 
     * @param propertyName the name of the property
     * @param listener the listener to add
     */

    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        if (listener instanceof WeakListener) {
            listener = this.getWeakWrapper(listener);
        }
        if (children == null) {
            children = new HashMap<String, BnPropertyChangeSupport>();
        }
        BnPropertyChangeSupport child = children.get(propertyName);
        if (child == null) {
            child = new BnPropertyChangeSupport(source);
            children.put(propertyName, child);
        }
        child.addPropertyChangeListener(listener);
    }

    /**
     * Removes the given {@link PropertyChangeListener} for a specific property.
     * 
     * @param propertyName the name of the property
     * @param listener the listener to remove
     */
    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        if (children == null) {
            return;
        }
        if (listener instanceof WeakListener) {
            listener = this.getWeakWrapper(listener);
            this.removeWeakWrapper((WeakWrapper)listener);
        }

        BnPropertyChangeSupport child = children.get(propertyName);
        if (child == null) {
            return;
        }
        child.removePropertyChangeListener(listener);
    }

    /**
     * Report a bound property update to each registered listener. No event is
     * fired if old and new are equal and non-null.
     * 
     * @param propertyName the property name of the property that was changed
     * @param oldValue the old value of the property
     * @param newValue the new value of the property
     */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        this.firePropertyChange(propertyName, oldValue, newValue, null);
    }

    /**
     * Report a bound property update to each registered listener. No event is
     * fired if old and new are equal and non-null.
     * 
     * @param propertyName the property name of the property that was changed
     * @param oldValue the old value of the property
     * @param newValue the new value of the property
     * @param cause the event that triggered the new event
     */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue, EventObject cause) {
        if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
            return;
        }

        List<PropertyChangeListener> targets = null;
        BnPropertyChangeSupport child = null;
        synchronized (this) {
            if (listeners != null) {
                targets = new ArrayList<PropertyChangeListener>(listeners);
            }
            if (children != null && propertyName != null) {
                child = children.get(propertyName);
            }
        }

        BnPropertyChangeEvent evt = new BnPropertyChangeEvent(source, propertyName, oldValue, newValue, cause);

        if (targets != null) {
            for (int i = 0; i < targets.size(); i++) {
                PropertyChangeListener target = (PropertyChangeListener)targets.get(i);
                target.propertyChange(evt);
            }
        }

        if (child != null) {
            child.firePropertyChange(evt);
        }
    }

    /**
     * Fire thw given {@link PropertyChangeEvent} to each registered listener.
     * No event is fired if the given event's old and new values are equal and
     * non-<code>null</code>.
     * 
     * @param evt the <code>PropertyChangeEvent</code> object
     */
    public void firePropertyChange(BnPropertyChangeEvent evt) {
        Object oldValue = evt.getOldValue();
        Object newValue = evt.getNewValue();
        String propertyName = evt.getPropertyName();
        if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
            return;
        }

        List<PropertyChangeListener> targets = null;
        BnPropertyChangeSupport child = null;
        synchronized (this) {
            if (listeners != null) {
                targets = new ArrayList<PropertyChangeListener>(listeners);
            }
            if (children != null && propertyName != null) {
                child = children.get(propertyName);
            }
        }

        if (targets != null) {
            for (int i = 0; i < targets.size(); i++) {
                PropertyChangeListener target = targets.get(i);
                target.propertyChange(evt);
            }
        }
        if (child != null) {
            child.firePropertyChange(evt);
        }
    }

    /**
     * Returns <code>true</code> if there are any listeners for the property
     * with the specified name.
     * 
     * @param propertyName the property name
     * @return <code>true</code> if there are any listeners
     */
    public synchronized boolean hasListeners(String propertyName) {
        if (listeners != null && !listeners.isEmpty()) {
            // there is a generic listener
            return true;
        }
        if (children != null) {
            BnPropertyChangeSupport child = children.get(propertyName);
            if (child != null && child.listeners != null) {
                return !child.listeners.isEmpty();
            }
        }
        return false;
    }

    /**
     * Returns <code>true</code> if there are any listeners registered at all.
     * 
     * @return <code>true</code> if there are any listeners for any property
     */
    public boolean hasListeners() {
        return !((this.listeners == null || this.listeners.isEmpty()) && (this.children == null || this.children.isEmpty()));
    }

    /**
     * Returns the {@link WeakWrapper} for the given listener.
     * 
     * @param l
     * @return the WeakWrapper for the given listener
     */
    private WeakWrapper getWeakWrapper(PropertyChangeListener listener) {
        WeakWrapper result = this.weakWrapper.get(listener);
        if (result == null) {
            result = new WeakWrapper(listener);
            this.weakWrapper.put(listener, result);
        }
        return result;
    }

    /**
     * Removes the given {@link WeakWrapper} from the weakWrapper cache.
     * 
     * @param wrapper
     */
    private void removeWeakWrapper(WeakWrapper wrapper) {
        this.weakWrapper.remove(wrapper);
    }

    /**
     * Removes all unused WeakWrapper instances from the given list of
     * listeners.
     * 
     * @param listeners
     */
    private void removeUnusedWeakWrappers(Collection<PropertyChangeListener> listeners) {
        List<PropertyChangeListener> toRemove = new ArrayList<PropertyChangeListener>();
        for (PropertyChangeListener l : listeners) {
            if (l instanceof WeakWrapper && ((WeakWrapper)l).isCleared()) {
                toRemove.add(l);
            }
        }
        listeners.removeAll(toRemove);
    }

    /**
     * The {@link WeakWrapper} is a delegator that forwards
     * {@link PropertyChangeEvent}s to a weakly referenced delegate object as
     * long as that reference has not been cleared.
     */
    private static class WeakWrapper implements PropertyChangeListener {
        private WeakReference<PropertyChangeListener> ref;

        /**
         * Constructs a {@link WeakWrapper} for the given delegate.
         * 
         * @param aDelegate
         */
        WeakWrapper(PropertyChangeListener aDelegate) {
            this.setDelegate(aDelegate);
        }

        /**
         * Returns <code>true</code>, if the delegate reference has been
         * cleared.
         * 
         * @return <code>true</code>, if the delegate reference has been cleared
         */
        boolean isCleared() {
            return this.ref.get() == null;
        }

        /** {@inheritDoc} */
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            PropertyChangeListener listener = this.getDelegate();
            if (listener == null) {
                return;
            }
            listener.propertyChange(evt);
        }

        /**
         * Returns the delegate.
         * 
         * @return the delegate
         */
        PropertyChangeListener getDelegate() {
            if (this.ref == null) {
                return null;
            }
            return this.ref.get();
        }

        /**
         * Sets the delegate.
         * 
         * @param aDelegate
         */
        void setDelegate(PropertyChangeListener aDelegate) {
            if (aDelegate == null) {
                this.ref = null;
            } else {
                this.ref = new WeakReference<PropertyChangeListener>(aDelegate);
            }
        }
    }
}