/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
/*
 * This class is slightly based on Sun's PropertyChangeSupport source which has
 * the following copyright notice: Copyright 2002 Sun Microsystems, Inc. All
 * rights reserved. SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license
 * terms.
 */
package org.beanfabrics.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This is a utility class that can be used by beans that support bound
 * properties.
 * 
 * @author Michael Karneim
 */
public class BnPropertyChangeSupport {
    private final WeakHashMap<PropertyChangeListener, WeakWrapper> weakWrapper = new WeakHashMap<PropertyChangeListener, WeakWrapper>();
    private List<PropertyChangeListener> listeners;

    /**
     * {@link HashMap} for managing listeners for specific properties. Maps
     * property names to {@link PropertyChangeSupport} objects.
     */
    private Map<String, BnPropertyChangeSupport> children;

    /**
     * The object to be provided as the "source" for any generated events.
     * 
     * @serial
     */
    private Object source;

    /**
     * Constructs a <code>PropertyChangeSupport</code>.
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
     * Add a {@link PropertyChangeListener} to the listener listCell. The
     * listener is registered for all properties.
     * 
     * @param listener the <code>PropertyChangeListener</code> to be added
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
     * Remove a {@link PropertyChangeListener} from the listener listCell. This
     * removes a {@link PropertyChangeListener} that was registered for all
     * properties.
     * 
     * @param listener the <code>PropertyChangeListener</code> to be removed
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
     * Add a {@link PropertyChangeListener} for a specific property. The
     * listener will be invoked only when a call on
     * <code>firePropertyChange</code> names that specific property.
     * 
     * @param propertyName the name of the property to listen on
     * @param listener the PropertyChangeListener to be added
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
     * Remove a {@link PropertyChangeListener} for a specific property.
     * 
     * @param propertyName the name of the property that was listened on
     * @param listener the PropertyChangeListener to be removed
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

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        this.firePropertyChange(propertyName, oldValue, newValue, null);
    }

    /**
     * Report a bound property update to any registered listeners. No event is
     * fired if old and new are equal and non-null.
     * 
     * @param propertyName the programmatic name of the property that was
     *            changed
     * @param oldValue the old value of the property
     * @param newValue the new value of the property
     * @param cause the event that caused the event
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
     * Fire an existing {@link PropertyChangeEvent} to any registered listeners.
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
     * Check if there are any listeners for a specific property.
     * 
     * @param propertyName the property name
     * @return <code>true</code> if there are ore or more listeners for the
     *         given property
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

    public boolean hasListeners() {
        return !((this.listeners == null || this.listeners.isEmpty()) && (this.children == null || this.children.isEmpty()));
    }

    private WeakWrapper getWeakWrapper(PropertyChangeListener l) {
        WeakWrapper result = this.weakWrapper.get(l);
        if (result == null) {
            result = new WeakWrapper(l);
            this.weakWrapper.put(l, result);
        }
        return result;
    }

    private void removeWeakWrapper(WeakWrapper wrapper) {
        this.weakWrapper.remove(wrapper);
    }

    private void removeUnusedWeakWrappers(List listeners) {
        LinkedList toRemove = new LinkedList();
        final int len = listeners.size();
        for (int i = 0; i < len; ++i) {
            Object l = listeners.get(i);
            if (l instanceof WeakWrapper && !((WeakWrapper)l).hasTarget()) {
                toRemove.add(l);
            }
        }
        listeners.removeAll(toRemove);
    }

    // //

    private static class WeakWrapper implements PropertyChangeListener {
        private WeakReference<PropertyChangeListener> ref;

        public WeakWrapper(PropertyChangeListener l) {
            this.setTarget(l);
        }

        public boolean hasTarget() {
            return this.ref.get() != null;
        }

        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            PropertyChangeListener listener = this.getTarget();
            if (listener == null) {
                return;
            }
            listener.propertyChange(evt);
        }

        protected PropertyChangeListener getTarget() {
            if (this.ref == null) {
                return null;
            }
            return this.ref.get();
        }

        protected void setTarget(PropertyChangeListener l) {
            if (l == null) {
                this.ref = null;
            } else {
                this.ref = new WeakReference<PropertyChangeListener>(l);
            }
        }
    }
}