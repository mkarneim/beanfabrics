/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import javax.swing.JRadioButton;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.model.IBooleanPM;
import org.beanfabrics.swing.internal.BooleanPMRadioButton;

/**
 * The <code>BnRadioButton</code> is a {@link JRadioButton} that can subscribe
 * to an {@link IBooleanPM}.
 * 
 * @author Michael Karneim
 * @beaninfo
 */
@SuppressWarnings("serial")
public class BnRadioButton extends BooleanPMRadioButton implements ModelSubscriber {
    private final Link link = new Link(this);

    /**
     * Constructs a <code>BnRadioButton</code>.
     */
    public BnRadioButton() {
    }

    /**
     * Constructs a <code>BnRadioButton</code> and binds it to the specified
     * model.
     * 
     * @param pModel the model
     */
    public BnRadioButton(IBooleanPM pModel) {
        super(pModel);
    }

    /**
     * Constructs a <code>BnRadioButton</code> and subscribes it for the model
     * at the specified Path provided by the given provider.
     * 
     * @param provider
     * @param path
     */
    public BnRadioButton(ModelProvider provider, Path path) {
        this.setModelProvider(provider);
        this.setPath(path);
    }

    /**
     * Constructs a <code>BnRadioButton</code> and subscribes it for the model
     * at the root level provided by the given provider.
     * 
     * @param provider
     */
    public BnRadioButton(ModelProvider provider) {
        this.setModelProvider(provider);
        this.setPath(new Path());
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