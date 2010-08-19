/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import java.util.Collection;
import java.util.Set;

/**
 * @param <E> the key type
 * @see MapPM
 * @author Michael Karneim
 */
public interface SelectedKeys<E> extends Set<E> {
    /**
     * Returns a new {@link Collection} with all selected keys. Modification on
     * this collection will not influence the original selection.
     * 
     * @return a new <code>Collection</code> with all selected keys
     */
    public Collection<E> toCollection();

    /**
     * Returns the first (topmost) selected key.
     * 
     * @return the first (topmost) selected key
     */
    public E getFirst();
}