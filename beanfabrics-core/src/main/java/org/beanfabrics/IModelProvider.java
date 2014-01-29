/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics;

import org.beanfabrics.event.ModelProviderListener;
import org.beanfabrics.model.PresentationModel;

/**
 * A container for an {@link PresentationModel} object.
 * {@link ModelProviderListener}s can subscribe for change events on the
 * structure of the presentation object model.
 * 
 * @author Michael Karneim
 */
public interface IModelProvider {
    /**
     * Returns the type of the PM, that is declared to be held at the root node of this provider.
     * Never returns <code>null</code>.
     * 
     * @return the type of the PM, that is declared to be held at the root node of this provider
     */
    public Class<? extends PresentationModel> getPresentationModelType();

    /**
     * Declares the type of the PM, that should be held at the root node of this provider.
     * 
     * @param newType the type of the PM
     */
    public void setPresentationModelType(Class<? extends PresentationModel> newType);

    /**
     * Returns the PM instance hold by this container.
     * TODO: remove this unsafe type cast, or declare the type parameter at class level.
     * 
     * @param <T> the provided PM type
     * @return the PM instance hold by this container
     */
    public <T extends PresentationModel> T getPresentationModel();

    /**
     * Sets the given PM instance as root node into this provider.
     * 
     * @param pm the PM
     */
    public void setPresentationModel(PresentationModel pm);

    /**
     * Returns the PM found at the end of the specified path or
     * <code>null</code> if nothing is found.
     * 
     * @param <T> the provided PM type
     * @param path the path to the target PM relative to the root node held by this provider
     * @return the PM found at the end of the specified path or
     *         <code>null</code> if nothing was found
     */
    public <T extends PresentationModel> T getPresentationModel(Path path);

    /**
     * Adds a {@link ModelProviderListener} to the listener list. The listener is
     * registered for all structural changes along the given path and will be
     * informed whenever the model reference at the end of the path changes.
     * 
     * @param path the path which references the presentation model
     * @param l the listener to add
     */
    public void addModelProviderListener(Path path, ModelProviderListener l);

    /**
     * Removes a {@link ModelProviderListener} from the listener list that was
     * registered for a specific path.
     * 
     * @param path the path which references the presentation model
     * @param l the listener to remove
     */
    public void removeModelProviderListener(Path path, ModelProviderListener l);
}