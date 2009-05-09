/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.swing;

import javax.swing.JToggleButton;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.model.IBooleanPM;
import org.beanfabrics.swing.internal.BooleanPMToggleButton;

/**
 * The <code>BnToggleButton</code> is a {@link JToggleButton} that can subscribe to an {@link IBooleanPM}.
 * 
 * @author Max Gensthaler
 * 
 * @beaninfo
 */
@SuppressWarnings("serial")
public class BnToggleButton extends BooleanPMToggleButton implements ModelSubscriber {
	private final Link link = new Link(this);

	/**
	 * Constructs a <code>BnToggleButton</code>.
	 */
	public BnToggleButton() {
	}
	
	/**
	 * Constructs a <code>BnToggleButton</code> and binds it
	 * to the specified model.
	 * @param pModel the model
	 */
	public BnToggleButton(IBooleanPM pModel) {
		super( pModel);
	}
	
	/**
	 * Constructs a <code>BnToggleButton</code> and subscribes it 
	 * for the model at the specified Path provided by the given provider.
	 * @param provider
	 * @param path
	 */
	public BnToggleButton(ModelProvider provider, Path path) {
		this.setModelProvider(provider);
		this.setPath(path);
	}

	/**
	 * Constructs a <code>BnToggleButton</code> and subscribes it 
	 * for the model at the root level provided by the given provider.
	 * @param provider
	 */
	public BnToggleButton(ModelProvider provider) {
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