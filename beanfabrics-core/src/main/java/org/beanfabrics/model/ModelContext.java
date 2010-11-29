/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import org.beanfabrics.context.DefaultContext;
import org.beanfabrics.log.Logger;
import org.beanfabrics.log.LoggerFactory;

/**
 * The {@link ModelContext} is the context implementation of a
 * {@link PresentationModel}.
 * 
 * @author Michael Karneim
 */
public class ModelContext extends DefaultContext {
    private final static Logger LOG = LoggerFactory.getLogger(ModelContext.class);
    private final PresentationModel owner;

    /**
     * Constructs a {@link ModelContext} for the given presentation model.
     * 
     * @param owner the presentation model that will be the owner of this
     *            context.
     */
    public ModelContext(PresentationModel owner) {
        this.owner = owner;
        if (LOG.isDebugEnabled()) {
            LOG.debug("creating ModelContext for " + owner);
        }
    }

    /** {@inheritDoc} */
    public String toString() {
        return "ModelContext(owner=" + owner + ")";
    }
}