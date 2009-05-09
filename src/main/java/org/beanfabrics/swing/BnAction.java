/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.swing;

import javax.swing.Action;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.swing.internal.OperationPMAction;

/**
 * The BnAction is an {@link Action} that can subscribe to an {@link IOperationPM}.
 *
 * @author Michael Karneim
 * 
 * @beaninfo
 */
@SuppressWarnings("serial")
public class BnAction extends OperationPMAction implements ModelSubscriber {
	private final Link link = new Link(this);
	
	/**
	 * Constructs a new instance of this class.
	 */
	public BnAction() {
		super();
	}
	
	/**
	 * Constructs a <code>BnAction</code> and binds it to
	 * the specified model.
	 * @param pModel the model
	 */
	public BnAction(IOperationPM pModel) {
		super(pModel);
	}

	/**
	 * Constructs a <code>BnAction</code> and subscribes it 
	 * for the model at the specified Path provided by the given provider.
	 * @param provider the <code>ModelProvider</code> to set initially
	 * @param path the <code>Path</code> to set initially
	 */
	public BnAction(IModelProvider provider, Path path) {
		this.setModelProvider(provider);
		this.setPath(path);
	}
	
	/**
	 * Constructs a <code>BnAction</code> and subscribes it 
	 * for the model at the root level provided by the given provider.
	 * @param provider the <code>ModelProvider</code> to set initially
	 */
	public BnAction(IModelProvider provider) {
		this.setModelProvider(provider);
		setPath(new Path());
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