/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.context;

/**
 * A {@link ServiceEntry} describes the attributes of a service entry in a specific context.
 * It provides access to the service itself as well as information about the location, the
 * type and the distance.
 * 
 * @author Michael Karneim
 */
public class ServiceEntry {
	private final Context origin;
	private final int distance;
	private final Object service;
	private final Class<?> type;
	/**
	 * Constructs a {@link ServiceEntry}.
	 * @param distance
	 * @param origin
	 * @param service
	 * @param type
	 */
	public ServiceEntry(int distance, Context origin, Object service, Class<?> type) {
		super();
		if (type == null) {
			throw new IllegalArgumentException("type==null");
		}
		if (service != null && type.isInstance(service) == false) {
			throw new IllegalArgumentException("service must be instance of " + type.getName() + " but was "
					+ service.getClass().getName());
		}
		this.distance = distance;
		this.origin = origin;
		this.service = service;
		this.type = type;
	}

	public Context getOrigin() {
		return origin;
	}

	public int getDistance() {
		return distance;
	}

	public Object getService() {
		return service;
	}

	public Class<?> getType() {
		return type;
	}
}