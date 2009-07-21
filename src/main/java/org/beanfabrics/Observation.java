/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.beanfabrics.model.PresentationModel;

/**
 * The observation observes all presentation models that are nodes along the
 * specified path (using the specified presentation model object as root node)
 * and notifies any PropertyChangeListener whenever a node reference changes.
 * 
 * @author Michael Karneim
 */
public class Observation extends AbstractBean {
    private PresentationModel source;
    private Path path;
    private PathEvaluation evaluation;
    private PresentationModel target;
    private PropertyChangeListener pcl;
    private boolean started;

    /**
     * Creates a new Observation along the given {@link Path} starting at the
     * given root node.
     * 
     * @param rootNode
     * @param path
     */
    public Observation(PresentationModel rootNode, Path path) {
        if (rootNode == null) {
            throw new IllegalArgumentException("source==null");
        }
        if (path == null) {
            throw new IllegalArgumentException("path==null");
        }
        this.source = rootNode;
        this.path = path;
        start();
    }

    /**
     * Returns <code>true</code> if this observation has been started.
     * 
     * @return <code>true</code> if this observation has been started
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Stops this observation.
     */
    public void stop() {
        if (!started) {
            return;
        }
        this.removeListener();
        started = false;
    }

    /**
     * Start this observation (again).
     */
    public void start() {
        if (started) {
            return;
        }
        evaluate();
        addListener();
        started = true;
    }

    /**
     * Returns <code>true</code> if the target of this observation's path is not
     * <code>null</code>.
     * 
     * @return <code>true</code> if the target of this observation's path is not
     *         <code>null</code>
     */
    public boolean hasTarget() {
        return this.target != null;
    }

    /**
     * Returns the target node specified by this observation's path relative to
     * the root node.
     * 
     * @return the target node specified by this observation's path relative to
     *         the root node
     */
    public PresentationModel getTarget() {
        return this.target;
    }

    private void evaluate() {
        this.evaluation = new PathEvaluation(source, path);
        if (evaluation.isCompletelyResolved()) {
            this.setTarget(evaluation.getResult().getValue());
        } else {
            this.setTarget(null);
        }
    }

    protected void setTarget(PresentationModel pModel) {
        PresentationModel old = this.target;
        if (pModel == old) {
            return;
        }
        this.target = pModel;
        this.getPropertyChangeSupport().firePropertyChange("target", old, pModel);
    }

    private void addListener() {
        for (PathEvaluation.Entry entry : this.evaluation) {
            if (entry.getOwner() != null && entry.getName() != null) {
                PresentationModel owner = entry.getOwner();
                String propertyName = entry.getName();
                owner.addPropertyChangeListener(propertyName, this.getPropertyChangeListener());
            }
        }
    }

    private void removeListener() {
        for (PathEvaluation.Entry entry : this.evaluation) {
            if (entry.getOwner() != null && entry.getName() != null) {
                PresentationModel owner = entry.getOwner();
                String propertyName = entry.getName();
                owner.removePropertyChangeListener(propertyName, this.getPropertyChangeListener());
            }
        }
    }

    private PropertyChangeListener getPropertyChangeListener() {
        if (this.pcl == null) {
            this.pcl = new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    Object oldValue = evt.getOldValue();
                    Object newValue = evt.getNewValue();
                    if (oldValue != newValue) {
                        stop();
                        start();
                    }
                }
            };
        }
        return this.pcl;
    }
}