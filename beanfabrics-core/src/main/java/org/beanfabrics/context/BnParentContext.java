/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.context;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.model.PresentationModel;

/**
 * The {@link BnParentContext} is a context that dynamically adds itself as a
 * parent context to the target's context.
 * <p>
 * Use this class if you want to dynamically provide access to a specific
 * service only to PM objects that are members of this context.
 * 
 * @author Michael Karneim
 */
// TODO (mk) UNIT TEST
public class BnParentContext extends DefaultContext implements View<PresentationModel>, ModelSubscriber {
    private final Link link = new Link(this);
    private PresentationModel presentationModel;

    /** {@inheritDoc} */
    public PresentationModel getPresentationModel() {
        return this.presentationModel;
    }

    /** {@inheritDoc} */
    public void setPresentationModel(PresentationModel aPresentationModel) {
        if (this.presentationModel != null) {
            this.presentationModel.getContext().removeParent(this);
        }
        this.presentationModel = aPresentationModel;
        if (this.presentationModel != null) {
            this.presentationModel.getContext().addParent(this);
        }
    }

    /** {@inheritDoc} */
    public IModelProvider getModelProvider() {
        return link.getModelProvider();
    }

    /** {@inheritDoc} */
    public void setModelProvider(IModelProvider provider) {
        this.link.setModelProvider(provider);
    }

    /** {@inheritDoc} */
    public Path getPath() {
        return link.getPath();
    }

    /** {@inheritDoc} */
    public void setPath(Path path) {
        this.link.setPath(path);
    }
}