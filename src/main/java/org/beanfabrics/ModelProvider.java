/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.beanfabrics.event.ModelProviderEvent;
import org.beanfabrics.event.ModelProviderListener;
import org.beanfabrics.model.PresentationModel;

/**
 * The default implementation of a {@link IModelProvider}.
 * 
 * @author Michael Karneim
 * @beaninfo
 */
public class ModelProvider extends AbstractBean implements IModelProvider {
    private List<Subscription> subscriptions = new LinkedList<Subscription>();
    private PresentationModel presentationModel;
    private Class<? extends PresentationModel> presentationModelType;

    /**
     * Constructs an empty <code>ModelProvider</code>.
     */
    public ModelProvider() {
        //
    }

    /**
     * Constructs an empty <code>ModelProvider</code> for the given
     * presentationModel type.
     */
    public ModelProvider(Class<? extends PresentationModel> presentationModelType) {
        if (presentationModelType == null) {
            throw new IllegalArgumentException("presentationModelType may not be null");
        }
        this.setPresentationModelType(presentationModelType);
    }

    /**
     * Constructs a <code>ModelProvider</code> with the given presentationModel.
     */
    public ModelProvider(PresentationModel presentationModel) {
        if (presentationModel == null) {
            throw new IllegalArgumentException("presentationModel must not be null");
        }
        this.setPresentationModelType(presentationModel.getClass());
        this.setPresentationModel(presentationModel);
    }

    @SuppressWarnings("unchecked")
    public <T extends PresentationModel> T getPresentationModel() {
        return (T)this.presentationModel;
    }

    /** {@inheritDoc} */
    public void setPresentationModel(PresentationModel newPresentationModel) {
        if (this.presentationModelType != null && newPresentationModel != null) {
            if (this.presentationModelType.isInstance(newPresentationModel) == false) {
                throw new IllegalArgumentException("the new presentationModel must be instance of " + presentationModelType.getName() + " but was " + newPresentationModel.getClass().getName());
            }
        }
        PresentationModel old = this.presentationModel;
        if (old == newPresentationModel) {
            return;
        }
        if (old != null) {
            for (Subscription b : subscriptions) {
                b.stopObservation();
            }
        }
        this.presentationModel = newPresentationModel;
        if (this.presentationModel != null) {
            for (Subscription b : subscriptions) {
                b.startObservation();
            }
        }
        this.getPropertyChangeSupport().firePropertyChange("presentationModel", old, newPresentationModel);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public <T extends PresentationModel> T getPresentationModel(Path path) {
        PathEvaluation eval = new PathEvaluation(this.presentationModel, path);
        if (eval.isCompletelyResolved()) {
            return (T)eval.getResult().getValue();
        } else {
            return null;
        }
    }

    /** {@inheritDoc} */
    public Class<? extends PresentationModel> getPresentationModelType() {
        if (this.presentationModelType == null) {
            if (this.presentationModel == null) {
                return PresentationModel.class;
            } else {
                return this.presentationModel.getClass();
            }
        }
        return this.presentationModelType;
    }

    /** {@inheritDoc} */
    public void setPresentationModelType(Class<? extends PresentationModel> newType) {
        Class<? extends PresentationModel> old = this.presentationModelType;
        if (newType.equals(old)) {
            return;
        }
        if (presentationModel != null && !newType.isInstance(presentationModel)) {
            throw new IllegalArgumentException("the presentationModel is already set; a new presentationModel type must be superclass of " + this.presentationModel.getClass().getName());
        }
        this.presentationModelType = newType;
        this.getPropertyChangeSupport().firePropertyChange("presentationModelType", old, newType);
    }

    /** {@inheritDoc} */
    public void addModelProviderListener(Path path, ModelProviderListener l) {
        Subscription binding = new Subscription(path, l);
        List<Subscription> newBindings = new LinkedList<Subscription>(this.subscriptions);
        newBindings.add(binding);
        this.subscriptions = newBindings;
        if (this.presentationModel != null) {
            binding.startObservation();
        }
    }

    /** {@inheritDoc} */
    public void removeModelProviderListener(Path path, ModelProviderListener l) {
        List<Subscription> newBindings = new LinkedList<Subscription>(this.subscriptions);
        Iterator<Subscription> it = newBindings.iterator();
        Subscription toRemove = null;
        while (it.hasNext()) {
            Subscription b = it.next();
            if (b.path.equals(path) && b.listener == l) {
                it.remove();
                toRemove = b;
                break;
            }
        }
        if (toRemove != null) {
            subscriptions = newBindings;
            toRemove.stopObservation();
        }
    }

    /**
     * A subscription is responsible for notifying a
     * {@link ModelProviderListener} whenever a reference on a given
     * {@link Path} changes.
     */
    private class Subscription implements PropertyChangeListener {
        final Path path;
        final ModelProviderListener listener;
        private Observation observation;

        /**
         * Creates a new subscription.
         * 
         * @param path
         * @param listener
         */
        public Subscription(Path path, ModelProviderListener listener) {
            super();
            this.path = path;
            this.listener = listener;
        }

        /**
         * Starts the observation of the presentation model (along the path)
         */
        public void startObservation() {
            this.observation = new Observation(presentationModel, this.path);
            this.observation.addPropertyChangeListener("target", this);
            if (this.observation.getTarget() != null) {
                listener.modelGained(new ModelProviderEvent(ModelProvider.this, ModelProvider.this.presentationModel, path));
            }
        }

        /**
         * Stops the observation.
         */
        public void stopObservation() {
            if (this.observation != null) {
                this.observation.removePropertyChangeListener("target", this);
                if (this.observation.getTarget() != null) {
                    listener.modelLost(new ModelProviderEvent(ModelProvider.this, ModelProvider.this.presentationModel, path));
                }
                this.observation.stop();
                this.observation = null;
            }
        }

        /** {@inheritDoc} */
        public void propertyChange(PropertyChangeEvent evt) {
            assert ("target".equals(evt.getPropertyName()));
            PresentationModel pModel = observation.getTarget();
            if (pModel == null) {
                listener.modelLost(new ModelProviderEvent(ModelProvider.this, ModelProvider.this.presentationModel, path));
            } else {
                listener.modelGained(new ModelProviderEvent(ModelProvider.this, ModelProvider.this.presentationModel, path));
            }
        }
    }
}