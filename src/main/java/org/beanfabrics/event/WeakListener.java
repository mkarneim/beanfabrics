/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.event;

/**
 * Marker interface that indicates, that marked instances (listeners) should be
 * treated as weak references, that means, that when an instance is found to be
 * only weak reachable and reachable by the listener listCell, it can be removed
 * safely from the listener listCell.
 * 
 * @author Michael Karneim
 */
public interface WeakListener {
}