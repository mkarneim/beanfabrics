/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics;

import java.beans.PropertyChangeListener;

import org.beanfabrics.event.BnPropertyChangeSupport;

/**
 * Implements common behaviors of beans.
 * 
 * @author Michael Karneim
 */
public abstract class AbstractBean implements Bean {
    // TODO (mk) think about making a beanfabrics style support
    private final BnPropertyChangeSupport propertyChangeSupport = new BnPropertyChangeSupport(this);

    /** {@inheritDoc} */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        this.propertyChangeSupport.addPropertyChangeListener(l);
    }

    /** {@inheritDoc} */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        this.propertyChangeSupport.removePropertyChangeListener(l);
    }

    /** {@inheritDoc} */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener l) {
        this.propertyChangeSupport.addPropertyChangeListener(propertyName, l);
    }

    /** {@inheritDoc} */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener l) {
        this.propertyChangeSupport.removePropertyChangeListener(propertyName, l);
    }

    /** {@inheritDoc} */
    public BnPropertyChangeSupport getPropertyChangeSupport() {
        return this.propertyChangeSupport;
    }

    /**
     * Indicates whether the specified objects are both null or "equal to" each
     * other.
     * 
     * @param a
     * @param b
     * @return true if the objects are both null or equal to each other.
     */
    protected static boolean equals(Object a, Object b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }
}