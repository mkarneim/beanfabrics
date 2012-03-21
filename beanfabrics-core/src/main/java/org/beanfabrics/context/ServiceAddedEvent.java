/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.context;

/**
 * An event which indicates that a service entry has been added to the event
 * source.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ServiceAddedEvent extends ContextEvent {
    private final ServiceEntry serviceEntry;

    /**
     * Constructs a {@link ServiceAddedEvent}.
     * 
     * @param source
     * @param serviceEntry
     */
    public ServiceAddedEvent(Context source, ServiceEntry serviceEntry) {
        super(source);
        this.serviceEntry = serviceEntry;
    }

    /**
     * Returns the service entry that has been added to this event's source.
     * 
     * @return the service entry that has been added to this event's source
     */
    public ServiceEntry getServiceEntry() {
        return serviceEntry;
    }
}