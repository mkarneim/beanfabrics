/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.event;

import java.util.EventObject;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.PresentationModel;

/**
 * An event which indicates that some change has occurred in some
 * {@link IModelProvider}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ModelProviderEvent extends EventObject {
    private final PresentationModel model;
    private final Path path;

    /**
     * Constructs a {@link ModelProviderEvent}.
     * 
     * @param source
     * @param aModel
     * @param path
     */
    public ModelProviderEvent(IModelProvider source, PresentationModel aModel, Path path) {
        super(source);
        this.model = aModel;
        this.path = path;
    }

    /**
     * The model involed in this event.
     * 
     * @return model involed in this event.
     */
    public PresentationModel getModel() {
        return this.model;
    }

    /**
     * The path involved in this event.
     * 
     * @return path involved in this event
     */
    public Path getPath() {
        return path;
    }
}