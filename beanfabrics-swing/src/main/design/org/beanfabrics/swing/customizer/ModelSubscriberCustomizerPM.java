/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer;

import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.swing.customizer.path.PathPM;
import org.beanfabrics.swing.customizer.util.CustomizerUtil;

/**
 * The <code>ModelSubscriberCustomizerPM</code> is the presentation model for the {@link ModelSubscriberCustomizer}.
 * 
 * @author Michael Karneim
 */
public class ModelSubscriberCustomizerPM extends AbstractCustomizerPM {
    private CustomizerBase customizer;
    private ModelSubscriber subscriber;

    protected final PathPM path = new PathPM();

    public ModelSubscriberCustomizerPM() {
        PMManager.setup(this);
    }

    public void setCustomizer(CustomizerBase customizer) {
        this.customizer = customizer;
        setModelSubscriber((ModelSubscriber) customizer.getObject());
    }

    public void setModelSubscriber(ModelSubscriber aSubscriber) {
        this.subscriber = aSubscriber;
        this.path.setPathContext(CustomizerUtil.getPathContextFromSubscriber(subscriber));
    }

    @OnChange(path = "path")
    void applyPath() {
        if (path.isValid() && customizer != null) {
            Path oldValue = subscriber.getPath();
            Path newValue = path.getPath();
            subscriber.setPath(newValue);
            customizer.firePropertyChange("path", oldValue, newValue);
        }
    }

}