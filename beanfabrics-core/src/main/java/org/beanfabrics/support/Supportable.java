/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.support;

/**
 * The {@link Supportable} interface declares the interface for classes that can
 * be enhanced dynamically by any {@link Support} object.
 * <p>
 * The feature lets you extend the logical interface of a specific object. For
 * an example see {@link PropertySupport}.
 * <p>
 * For adding a <code>Support</code> object call
 * {@link SupportMap#put(Class, Support) getSupportMap().put(Class, Support)},
 * for accessing an existing support object call {@link SupportMap#get(Class)
 * getSupportMap().get(Class)} where Class is the type of the
 * <code>Support</code>.
 * 
 * @author Michael Karneim
 */
public interface Supportable {
    /**
     * Returns the {@link SupportMap} of this object.
     * 
     * @return the {@link SupportMap} of this object
     */
    public SupportMap getSupportMap();
}