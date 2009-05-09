/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.event;

import java.util.EventObject;

import org.beanfabrics.Path;
import org.beanfabrics.model.PresentationModel;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ModelProviderEvent extends EventObject {
	private final PresentationModel contentRoot;
	private final Path path;

	public ModelProviderEvent(Object source, PresentationModel contentRoot, Path path) {
		super(source);
		this.contentRoot = contentRoot;
		this.path = path;
	}

	public PresentationModel getContentRoot() {
		return this.contentRoot;
	}

	public Path getPath() {
		return path;
	}
}