/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.context;

/**
 * A class that implements this interface has a context property.
 * 
 * @see Context
 * @author Michael Karneim
 */
public interface ContextOwner {
    /**
     * Returns the {@link Context} of this model.
     * 
     * @return the IContext of this model
     */
    public Context getContext();
}