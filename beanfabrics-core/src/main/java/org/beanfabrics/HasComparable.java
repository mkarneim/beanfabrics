/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics;

/**
 * The {@link HasComparable} is the interface of classes that do not implement
 * the {@link Comparable} interface itself but provide access to a comparable
 * substitute.
 * 
 * @author Michael Karneim
 */
public interface HasComparable {
    /**
     * Returns the comparable substitute. This substitute can be used for
     * comparison of this object with another.
     * 
     * @return the comparable substitute
     */
    public Comparable<?> getComparable();
}
