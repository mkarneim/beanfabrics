/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.context;

import org.beanfabrics.log.Logger;
import org.beanfabrics.log.LoggerFactory;
import org.beanfabrics.model.PresentationModel;

/**
 * @author Michael Karneim
 */
public class ModelContext extends DefaultContext {
	private final static Logger LOG = LoggerFactory.getLogger(ModelContext.class);
	private final PresentationModel owner;

	public ModelContext(PresentationModel owner) {
		this.owner = owner;
		if (LOG.isDebugEnabled()) {
			LOG.debug("creating ModelContext for " + owner);
		}
	}

	public String toString() {
		return "ModelContext(owner="+owner+")";
	}
}