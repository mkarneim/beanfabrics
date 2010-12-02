/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.event;

import org.beanfabrics.IModelProvider;

/**
 * The listener interface for receiving {@link ModelProviderEvent}s.
 * <p>
 * A listener object created from this class can be registered with a
 * {@link IModelProvider} using the
 * {@link IModelProvider#addModelProviderListener(org.beanfabrics.Path, ModelProviderListener)}
 * method.
 * 
 * @author Michael Karneim
 */
public interface ModelProviderListener {
    /**
     * Invoked after a presentation model has been made available at a specific
     * path.
     * 
     * @param evt
     */
    public void modelGained(ModelProviderEvent evt);

    /**
     * Invoked after a presentation model has been made unavailable at a
     * specific path.
     * 
     * @param evt
     */
    public void modelLost(ModelProviderEvent evt);
}