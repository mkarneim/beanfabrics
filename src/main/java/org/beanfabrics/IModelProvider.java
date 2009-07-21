/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
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
     * Returns the type of the presentation model object.
     * 
     * @return the type of the presentation model object
     */
    public Class<? extends PresentationModel> getPresentationModelType();

    /**
     * Sets the type of presentation model object.
     * 
     * @param newType the type of the presentation model to set
     */
    public void setPresentationModelType(Class<? extends PresentationModel> newType);

    /**
     * Returns the presentation model hold by this container.
     * 
     * @param <T> the provided presentation model type
     * @return the presentation model hold by this container
     */
    public <T extends PresentationModel> T getPresentationModel();

    /**
     * Sets the presentation model root object.
     * 
     * @param pModel the root presentation model to set
     */
    public void setPresentationModel(PresentationModel pModel);

    /**
     * Returns the presentation model found at the end of the specified path or
     * <code>null</code> if nothing is found.
     * 
     * @param <T> the provided presentation model type
     * @param path the path which references the presentation model
     * @return the presentation model found at the end of the specified path or
     *         <code>null</code> if nothing was found
     */
    public <T extends PresentationModel> T getPresentationModel(Path path);

    /**
     * Adds a ModelProviderListener to the listener list. The listener is
     * registered for all structural changes along the given path and will be
     * informed whenever the model reference at the end of the path changes.
     * 
     * @param path the path which references the presentation model
     * @param l the listener to add
     */
    public void addModelProviderListener(Path path, ModelProviderListener l);

    /**
     * Removes a ModelProviderListener from the listener list that was
     * registered for a specific path.
     * 
     * @param path the path which references the presentation model
     * @param l the listener to remove
     */
    public void removeModelProviderListener(Path path, ModelProviderListener l);
}