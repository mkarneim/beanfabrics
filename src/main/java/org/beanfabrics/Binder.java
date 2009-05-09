/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics;

import org.beanfabrics.model.PresentationModel;

/**
 * The Binder offers some convenient methods for binding a {@link ModelSubscriber} to a {@link PresentationModel}.
 *
 * @author Michael Karneim
 */
public class Binder {
	/**
	 * Binds the given {@link ModelSubscriber} to the {@link PresentationModel} specified by the given {@link Path}
	 * relative to the given root model.
	 * 
	 * @return the {@link ModelProvider} that manages the subscription.
	 */
	public static ModelProvider bind(ModelSubscriber subscriber, PresentationModel root, Path path) {
		ModelProvider provider = new ModelProvider(root);
		subscriber.setModelProvider(provider);
		subscriber.setPath(path);
		return provider;
	}

	/**
	 * Binds the given {@link ModelSubscriber} to the given {@link PresentationModel} 
	 *
	 * @return the {@link ModelProvider} that manages the subscription.
	 */
	public static ModelProvider bind(ModelSubscriber subscriber, PresentationModel presentationModel) {
		return bind(subscriber, presentationModel, new Path("this"));
	}
}