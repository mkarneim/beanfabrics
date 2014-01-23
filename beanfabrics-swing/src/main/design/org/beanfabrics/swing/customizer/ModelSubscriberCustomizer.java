/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer;

import java.awt.BorderLayout;
import java.beans.Customizer;

import javax.swing.JPanel;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;

/**
 * The <code>ModelSubscriberCustomizer</code> is a Java Beans {@link Customizer} for a {@link ModelSubscriber}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ModelSubscriberCustomizer<PM extends ModelSubscriberCustomizerPM> extends CustomizerBasePanel<PM> {
    private ModelProvider localProvider;
    private ModelSubscriberCustomizerPanel modelSubscriberCustomizerPanel;
    private JPanel centerPanel;

    protected ModelSubscriberCustomizer(PM pm) {
        super(pm);
        getLocalProvider().setPresentationModel(pm);
        setLayout(new BorderLayout());
        add(getCenterPanel(), BorderLayout.CENTER);
    }

    public ModelSubscriberCustomizer() {
        this((PM) new ModelSubscriberCustomizerPM());
    }

    private JPanel getCenterPanel() {
        if (centerPanel == null) {
            centerPanel = new JPanel();
            centerPanel.setBorder(null);
            centerPanel.setOpaque(false);
            centerPanel.setLayout(new BorderLayout(0, 0));
            centerPanel.add(getModelSubscriberCustomizerPanel(), BorderLayout.CENTER);
        }
        return centerPanel;
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
     * 
     * @wbp.nonvisual location=10,430
     * @return the local <code>ModelProvider</code>
     */
    protected ModelProvider getLocalProvider() {
        if (localProvider == null) {
            localProvider = new ModelProvider(); // @wb:location=16,477
            localProvider.setPresentationModelType(ModelSubscriberCustomizerPM.class);
        }
        return localProvider;
    }

    public ModelSubscriberCustomizerPanel getModelSubscriberCustomizerPanel() {
        if (modelSubscriberCustomizerPanel == null) {
            modelSubscriberCustomizerPanel = new ModelSubscriberCustomizerPanel();
            modelSubscriberCustomizerPanel.setPath(new Path("this"));
            modelSubscriberCustomizerPanel.setModelProvider(getLocalProvider());
        }
        return modelSubscriberCustomizerPanel;
    }
}