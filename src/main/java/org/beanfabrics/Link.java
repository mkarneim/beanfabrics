/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
package org.beanfabrics;

import org.beanfabrics.event.ModelProviderEvent;
import org.beanfabrics.event.ModelProviderListener;
import org.beanfabrics.log.Logger;
import org.beanfabrics.log.LoggerFactory;
import org.beanfabrics.model.PresentationModel;

/**
 * The Link is a view decorator that helps connecting a {@link View} to a {@link PresentationModel}
 * provided by a {@link IModelProvider} at a given {@link Path}.
 *
 * @author Michael Karneim
 */
public class Link implements ModelSubscriber {
	private static final Logger LOG = LoggerFactory.getLogger(Link.class);

	private final ModelProviderListener providerListener = new ModelProviderListener() {
		public void modelGained(ModelProviderEvent evt) {
			PathEvaluation eval = new PathEvaluation(provider.getPresentationModel(), path);
			assert( eval.isCompletelyResolved());
			PresentationModel pModel = eval.getResult().getValue();
			setPresentationModel(pModel);
		}
		public void modelLost(ModelProviderEvent evt) {
			setPresentationModel(null);
		}
	};

	private final View view;

	private final ViewClassDecorator viewClassDecorator;
	private IModelProvider provider;
	private Path path;

	/**
	 * Creates a Link for the given view.
	 * @param view
	 */
	public Link( View view) {
		if ( view == null) {
			throw new IllegalArgumentException("view==null");
		}
		this.view = view;
		this.viewClassDecorator = new ViewClassDecorator( view.getClass());
    	link();
	}
	/**
	 * {@inheritDoc}
	 */
	public void setModelProvider( IModelProvider provider) {
		unlink();
		this.provider = provider;
		link();
	}
	/**
	 * {@inheritDoc}
	 */
    public IModelProvider getModelProvider() {
    	return this.provider;
    }
    /**
     * {@inheritDoc}
     */
    public void setPath( Path path) {
    	unlink();
    	this.path = path;
    	link();
    }
    /**
     * {@inheritDoc}
     */
    public Path getPath() {
    	return this.path;
    }
    /**
     * Returns the view.
     * @return the view
     */
    public View getView() {
    	return this.view;
    }

    private void link() {
		if ( provider != null && path != null) {
			provider.addModelProviderListener(path, providerListener);
		}
	}

	private void unlink() {
		if ( provider != null && path != null) {
			provider.removeModelProviderListener(path, providerListener);
		}
		if ( view != null) {
			view.setPresentationModel( null);
		}
	}

    private void setPresentationModel( PresentationModel model) {
    	if ( view != null ) {
    		if ( accept(model)) {
	    		view.setPresentationModel(model);
	    		return;
    		} else {
    			LOG.warn("Can't set UI model of "+view.getClass().getName()
    					+". Expected model type is "+getExpectedModelType().getName()+", but got "+model.getClass().getName()
    					+". Setting UI model to null.");
    			view.setPresentationModel(null);
    		}
    	}
    }

	private boolean accept(PresentationModel model) {
		Class expected = getExpectedModelType();
		return ( expected == null || model == null || expected.isInstance(model));
	}

	private Class getExpectedModelType() {
		return this.viewClassDecorator.getExpectedModelType();
	}
}