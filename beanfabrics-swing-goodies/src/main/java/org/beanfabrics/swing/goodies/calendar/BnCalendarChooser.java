/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.goodies.calendar;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.IDatePM;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BnCalendarChooser extends CalendarChooser implements View<IDatePM>, ModelSubscriber {
    private PropertyChangeListener pcl = new MyWeakPropertyChangeListener();
    private class MyWeakPropertyChangeListener implements WeakPropertyChangeListener, Serializable {
        public void propertyChange(PropertyChangeEvent evt) {
            refresh();
        }
    };
    private final Link link = new Link(this);
    private IDatePM pModel;

    public BnCalendarChooser() {
    }

    /** {@inheritDoc} */
    public IDatePM getPresentationModel() {
        return this.pModel;
    }

    /** {@inheritDoc} */
    public void setPresentationModel(IDatePM newModel) {
        IDatePM oldModel = this.pModel;
        if (this.pModel != null) {
            this.pModel.removePropertyChangeListener(pcl);
        }
        this.pModel = newModel;
        if (this.pModel != null) {
            this.pModel.addPropertyChangeListener(pcl);
        }
        this.firePropertyChange("presentationModel", oldModel, newModel);
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

    protected void refresh() {
        if (this.pModel == null) {
            this.setSelectedDate(null);
        } else {
            this.setSelectedDate(this.pModel.getDate());
        }
    }
}
