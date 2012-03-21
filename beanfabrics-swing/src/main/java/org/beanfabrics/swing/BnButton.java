/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import javax.swing.JButton;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.swing.internal.OperationPMButton;

/**
 * The <code>BnButton</code> is a {@link JButton} that can subscribe to an
 * {@link IOperationPM}.
 * <p>
 * For an example about using BnButton, please see <a
 * href="http://www.beanfabrics.org/index.php/BnButton"
 * target="parent">http://www.beanfabrics.org/index.php/BnButton</a>
 * </p>
 * 
 * @author Michael Karneim
 * @beaninfo
 */
@SuppressWarnings("serial")
public class BnButton extends OperationPMButton implements ModelSubscriber {
    private final Link link = new Link(this);

    /**
     * Constructs a <code>BnButton</code>.
     */
    public BnButton() {
        super();
    }

    /**
     * Constructs a <code>BnButton</code> and binds it to the specified model
     * 
     * @param pModel the model
     */
    public BnButton(IOperationPM pModel) {
        super(pModel);
    }

    /**
     * Constructs a <code>BnButton</code> and subscribes it for the model at the
     * specified Path provided by the given provider.
     * 
     * @param provider
     * @param path
     */
    public BnButton(ModelProvider provider, Path path) {
        this.setModelProvider(provider);
        this.setPath(path);
    }

    /**
     * Constructs a <code>BnButton</code> and subscribes it for the model at the
     * root level provided by the given provider.
     * 
     * @param provider
     */
    public BnButton(ModelProvider provider) {
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