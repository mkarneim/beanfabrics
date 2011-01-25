/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.context;

import java.util.EventListener;

/**
 * The listener interface for receiving context events.
 * <p>
 * A listener object created from this class can be registered with a
 * {@link Context} using the {@link Context#addContextListener(ContextListener)}
 * method.
 * 
 * @author Michael Karneim
 */
public interface ContextListener extends EventListener {
    /**
     * Invoked when a parent context has been added to the event source.
     * 
     * @param evt
     */
    void parentAdded(ParentAddedEvent evt);

    /**
     * Invoked when a parent context has been removed from the event source.
     * 
     * @param evt
     */
    void parentRemoved(ParentRemovedEvent evt);

    /**
     * Invoked when a service entry has been added to the event source.
     * 
     * @param evt
     */
    void serviceAdded(ServiceAddedEvent evt);

    /**
     * Invoked when a service entry has been removed from the event source.
     * 
     * @param evt
     */
    void serviceRemoved(ServiceRemovedEvent evt);
}