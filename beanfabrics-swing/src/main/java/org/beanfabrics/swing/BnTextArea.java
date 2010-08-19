/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import javax.swing.JTextArea;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.swing.internal.TextPMTextArea;

/**
 * The <code>BnTextArea</code> is a {@link JTextArea} that can subscribe to an
 * {@link ITextPM}.
 * <p>
 * For an example about using BnTextArea, please see <a
 * href="http://www.beanfabrics.org/index.php/BnTextArea"
 * target="parent">http://www.beanfabrics.org/index.php/BnTextArea</a>
 * </p>
 * 
 * @author Michael Karneim
 * @beaninfo
 */
@SuppressWarnings("serial")
public class BnTextArea extends TextPMTextArea implements ModelSubscriber {
    private final Link link = new Link(this);

    /**
     * Constructs a <code>BnTextArea</code>.
     */
    public BnTextArea() {
    }

    /**
     * Constructs a <code>BnTextArea</code> and binds it to the specified model.
     * 
     * @param pModel the model
     */
    public BnTextArea(ITextPM pModel) {
        super(pModel);
    }

    /**
     * Constructs a <code>BnTextArea</code> and subscribes it for the model at
     * the specified Path provided by the given provider.
     * 
     * @param provider
     * @param path
     */
    public BnTextArea(ModelProvider provider, Path path) {
        this.setModelProvider(provider);
        this.setPath(path);
    }

    /**
     * Constructs a <code>BnTextArea</code> and subscribes it for the model at
     * the root level provided by the given provider.
     * 
     * @param provider
     */
    public BnTextArea(ModelProvider provider) {
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