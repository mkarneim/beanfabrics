/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.context;

/**
 * @author Michael Karneim
 */
public class ServiceEntry {
    private final Context origin;
    private final int distance;
    private final Object service;
    private final Class<?> type;

    public ServiceEntry(int distance, Context origin, Object service, Class<?> type) {
        super();
        if (type == null) {
            throw new IllegalArgumentException("type==null");
        }
        if (service != null && type.isInstance(service) == false) {
            throw new IllegalArgumentException("service must be instance of " + type.getName() + " but was " + service.getClass().getName());
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