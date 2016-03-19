/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.beanfabrics.event.BnPropertyChangeEvent;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.PresentationModel;

/**
 * Observer for the {@link PresentationModel} object located at the end of a
 * specified {@link Path} relatively to a {@link ModelProvider}'s root. Fires a
 * {@link PropertyChangeEvent} (with property name "presentationModel") whenever
 * the model reference or the model properties change. The method
 * {@link #getPresentationModel()} returns that model.
 *
 * @author Michael Karneim
 * @beaninfo
 */
// TODO JUNIT TEST
public class BnModelObserver extends AbstractBean implements View<PresentationModel>, ModelSubscriber {
    private final PropertyChangeListener listener = new WeakPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            BnPropertyChangeEvent nextEvent = new BnPropertyChangeEvent(BnModelObserver.this, evt.getPropertyName(), null, null, evt);
            getPropertyChangeSupport().firePropertyChange(nextEvent);
        }
    };
    private final Link link = new Link(this);
    private PresentationModel presentationModel;

    /**
     * Creates a new instance.
     */
    public BnModelObserver() {
    }

    /**
     * Returns whether this observer is connected to any
     * {@link PresentationModel}
     *
     * @return <code>true</code> if this component is connected, else
     *         <code>false</code>
     */
    boolean isConnected() {
        return this.presentationModel != null;
    }

    /** {@inheritDoc} */
    public PresentationModel getPresentationModel() {
        return presentationModel;
    }

    /** {@inheritDoc} */
    public void setPresentationModel(PresentationModel presentationModel) {
        PresentationModel oldEditor = this.presentationModel;
        if (this.presentationModel != null) {
            this.presentationModel.removePropertyChangeListener(listener);
        }
        this.presentationModel = presentationModel;
        if (this.presentationModel != null) {
            this.presentationModel.addPropertyChangeListener(listener);
        }
        if (AbstractBean.equals(oldEditor, this.presentationModel) == false) {
            this.getPropertyChangeSupport().firePropertyChange("presentationModel", oldEditor, this.presentationModel);
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
