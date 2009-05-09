/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.context;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ServiceAddedEvent extends ContextEvent {
	private final ServiceEntry serviceEntry;

	public ServiceAddedEvent(Context source, ServiceEntry serviceEntry) {
		super(source);
		this.serviceEntry = serviceEntry;
	}

	public ServiceEntry getServiceEntry() {
		return serviceEntry;
	}
}