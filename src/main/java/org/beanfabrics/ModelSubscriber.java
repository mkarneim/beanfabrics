/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics;

import org.beanfabrics.model.PresentationModel;

/**
 * Any class that implements this interface can subscribe to an
 * {@link PresentationModel} provided by a {@link IModelProvider} at a given
 * {@link Path}.
 * 
 * @author Michael Karneim
 */
public interface ModelSubscriber {
    /**
     * Returns the provider.
     * 
     * @return the <code>ModelProvider</code>
     */
    public IModelProvider getModelProvider();

    /**
     * Sets the provider.
     * 
     * @param provider the provider
     */
    public void setModelProvider(IModelProvider provider);

    /**
     * Returns the path.
     * 
     * @return the path
     */
    public Path getPath();

    /**
     * Sets the path. The {@link Path} is used to locate the model inside the
     * provider.
     * 
     * @param path the path relative to the provider's root model
     */
    public void setPath(Path path);
}