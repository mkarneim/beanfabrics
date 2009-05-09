/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.swing;

import javax.swing.JPasswordField;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.swing.internal.TextPMPasswordField;

/**
 * The <code>BnPasswordField</code> is a {@link JPasswordField} that can subscribe to an {@link ITextPM}.
 * 
 * @author Michael Karneim
 * 
 * @beaninfo
 */
// TODO (mk) UNIT TEST
@SuppressWarnings("serial")
public class BnPasswordField extends TextPMPasswordField implements ModelSubscriber {

	private final Link link = new Link(this);

	/**
	 * Constructs a <code>BnPasswordField</code>.
	 */
	public BnPasswordField() {
	}
	
	/**
	 * Constructs a <code>BnPasswordField</code> and binds it
	 * to the specified model.
	 * @param pModel the model
	 */
	public BnPasswordField(ITextPM pModel) {
		super(pModel);
	}
	
	/**
	 * Constructs a <code>BnPasswordField</code> and subscribes it 
	 * for the model at the specified Path provided by the given provider.
	 * @param provider
	 * @param path
	 */
	public BnPasswordField(ModelProvider provider, Path path) {
		this.setModelProvider(provider);
		this.setPath(path);
	}

	/**
	 * Constructs a <code>BnPasswordField</code> and subscribes it 
	 * for the model at the root level provided by the given provider.
	 * @param provider
	 */
	public BnPasswordField(ModelProvider provider) {
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