/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.util.Map;

/**
 * The {@link IMapPM} is the minimal interface for a presentation model that
 * maps key objects to presentation model objects.
 * 
 * @author Michael Karneim
 */
public interface IMapPM<K, V extends PresentationModel> extends IListPM<V> {
    /**
     * Returns a new {@link Map} with all elements of this map.
     * 
     * @return a new {@link Map} with all elements of this map.
     */
    public Map<K, V> toMap();
}