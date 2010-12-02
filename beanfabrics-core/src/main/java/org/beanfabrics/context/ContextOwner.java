/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.context;

/**
 * The {@link ContextOwner} is an interface for classes that can "own" a
 * {@link Context} and provide public access to it.
 * <p>
 * Please note that the owned context is assumed being constant. Implementors
 * must not change their context reference during lifetime.
 * 
 * @see Context
 * @author Michael Karneim
 */
public interface ContextOwner {
    /**
     * Returns the {@link Context}.
     * <p>
     * The context is assumed being constant. Implementors must not change their
     * context reference during lifetime.
     * 
     * @return the context.
     */
    public Context getContext();
}