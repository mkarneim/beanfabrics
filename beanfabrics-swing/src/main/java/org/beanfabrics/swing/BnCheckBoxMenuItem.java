/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import javax.swing.JMenuItem;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.model.IBooleanPM;
import org.beanfabrics.swing.internal.BooleanPMMenuItem;

/**
 * The <code>BnCheckBoxMenuItem</code> is a {@link JMenuItem} that can subscribe
 * to an {@link IBooleanPM}.
 * 
 * @author Max Gensthaler
 */
// TODO unit test
// TODO customizer - does not open???
@SuppressWarnings("serial")
public class BnCheckBoxMenuItem extends BooleanPMMenuItem implements ModelSubscriber {
    private final Link link = new Link(this);

    /**
     * Constructs a <code>BnCheckBoxMenuItem</code>.
     */
    public BnCheckBoxMenuItem() {
        super();
    }

    /**
     * Constructs a <code>BnCheckBoxMenuItem</code> and binds it to the
     * specified model.
     * 
     * @param pModel the model
     */
    public BnCheckBoxMenuItem(IBooleanPM pModel) {
        super(pModel);
    }

    /**
     * Constructs a <code>BnCheckBoxMenuItem</code> and subscribes it for the
     * model at the specified Path provided by the given provider.
     * 
     * @param provider
     * @param path
     */
    public BnCheckBoxMenuItem(ModelProvider provider, Path path) {
        this.setModelProvider(provider);
        this.setPath(path);
    }

    /**
     * Constructs a <code>BnCheckBoxMenuItem</code> and subscribes it for the
     * model at the root level provided by the given provider.
     * 
     * @param provider
     */
    public BnCheckBoxMenuItem(ModelProvider provider) {
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