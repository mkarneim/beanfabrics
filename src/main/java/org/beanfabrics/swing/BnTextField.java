/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import javax.swing.JTextField;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.swing.internal.TextPMTextField;

/**
 * The <code>BnTextField</code> is a {@link JTextField} that can subscribe to an
 * {@link ITextPM}.
 * <p>
 * For an example about using BnTextField, please see <a
 * href="http://www.beanfabrics.org/index.php/BnTextField"
 * target="parent">http://www.beanfabrics.org/index.php/BnTextField</a>
 * </p>
 * 
 * @author Michael Karneim
 * @beaninfo
 */
@SuppressWarnings("serial")
public class BnTextField extends TextPMTextField implements ModelSubscriber {

    private final Link link = new Link(this);

    /**
     * Constructs a <code>BnTextField</code>.
     */
    public BnTextField() {
    }

    /**
     * Constructs a <code>BnTextField</code> and binds it to the specified
     * model.
     * 
     * @param pModel the model.
     */
    public BnTextField(ITextPM pModel) {
        super(pModel);
    }

    /**
     * Constructs a <code>BnTextField</code> and subscribes it for the model at
     * the specified Path provided by the given provider.
     * 
     * @param provider
     * @param path
     */
    public BnTextField(ModelProvider provider, Path path) {
        this.setModelProvider(provider);
        this.setPath(path);
    }

    /**
     * Constructs a <code>BnTextField</code> and subscribes it for the model at
     * the root level provided by the given provider.
     * 
     * @param provider
     */
    public BnTextField(ModelProvider provider) {
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