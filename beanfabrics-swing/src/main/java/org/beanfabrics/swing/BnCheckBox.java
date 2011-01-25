/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import javax.swing.JCheckBox;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.model.IBooleanPM;
import org.beanfabrics.swing.internal.BooleanPMCheckBox;

/**
 * The <code>BnCheckBox</code> is a {@link JCheckBox} that can subscribe to an
 * {@link IBooleanPM}.
 * <p>
 * For an example about using BnCheckBox, please see <a
 * href="http://www.beanfabrics.org/index.php/BnCheckBox"
 * target="parent">http://www.beanfabrics.org/index.php/BnCheckBox</a>
 * </p>
 * 
 * @author Michael Karneim
 * @beaninfo
 */
@SuppressWarnings("serial")
public class BnCheckBox extends BooleanPMCheckBox implements ModelSubscriber {
    private final Link link = new Link(this);

    /**
     * Constructs a <code>BnCheckBox</code>
     */
    public BnCheckBox() {
    }

    /**
     * Constructs a <code>BnCheckBox</code> and binds it to the specified model.
     * 
     * @param pModel the model
     */
    public BnCheckBox(IBooleanPM pModel) {
        super(pModel);
    }

    /**
     * Constructs a <code>BnCheckBox</code> and subscribes it for the model at
     * the specified Path provided by the given provider.
     * 
     * @param provider
     * @param path
     */
    public BnCheckBox(ModelProvider provider, Path path) {
        this.setModelProvider(provider);
        this.setPath(path);
    }

    /**
     * Constructs a <code>BnCheckBox</code> and subscribes it for the model at
     * the root level provided by the given provider.
     * 
     * @param provider
     */
    public BnCheckBox(ModelProvider provider) {
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