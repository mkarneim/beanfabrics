/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.context;

/**
 * An event which indicates that a service entry has been removed from the event
 * source.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ServiceRemovedEvent extends ContextEvent {
    private final ServiceEntry serviceEntry;

    /**
     * Constructs a {@link ServiceRemovedEvent}.
     * 
     * @param source
     * @param serviceEntry
     */
    public ServiceRemovedEvent(Context source, ServiceEntry serviceEntry) {
        super(source);
        this.serviceEntry = serviceEntry;
    }

    /**
     * Returns the service entry that has been removed from this event's source.
     * 
     * @return the service entry that has been removed from this event's source
     */
    public ServiceEntry getServiceEntry() {
        return serviceEntry;
    }
}